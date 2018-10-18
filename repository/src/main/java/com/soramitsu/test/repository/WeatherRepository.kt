package com.soramitsu.test.repository

import com.soramitsu.test.domain.base.Trigger
import com.soramitsu.test.domain.exceptions.Error
import com.soramitsu.test.domain.exceptions.NetworkConnectionError
import com.soramitsu.test.domain.exceptions.NetworkErrorException
import com.soramitsu.test.domain.exceptions.RefreshDataError
import com.soramitsu.test.domain.interfaces.Executor
import com.soramitsu.test.domain.interfaces.WeatherRepository
import com.soramitsu.test.domain.models.City
import com.soramitsu.test.repository.datasource.api.ApiDataSource
import com.soramitsu.test.repository.datasource.db.DbDataSource
import com.soramitsu.test.repository.model.api.ApiCurrentWeatherResponse
import com.soramitsu.test.repository.model.api.ApiForecastWeatherResponse
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import io.reactivex.subjects.PublishSubject

class WeatherRepository(
    private val apiDataSource: ApiDataSource,
    private val dbDataSource: DbDataSource,
    private val executor: Executor
) : WeatherRepository {

    private val refreshTrigger: PublishSubject<Trigger> = PublishSubject.create()
    private val errorBus: PublishSubject<Error> = PublishSubject.create()

    init {
        executor
            .executeAsync(
                refreshTrigger
                    .startWith(Trigger)
                    .flatMapSingle { dbDataSource.getCitiesInDb() }
                    .filter { it.isNotEmpty() }
                    .flatMapSingle { cities ->
                        apiDataSource
                            .fetchCurrentWeatherForAll(cities)
                            .onErrorReturn {
                                handleException(it)
                                listOf()
                            }
                            .zipWith(
                                fetchForecastForAll(cities).onErrorReturn {
                                    handleException(it)
                                    listOf()
                                },
                                BiFunction { currentWeatherList: List<ApiCurrentWeatherResponse>,
                                             forecastList: List<ApiForecastWeatherResponse> ->
                                    currentWeatherList to forecastList
                                }
                            )
                            .doOnSuccess { (currentWeather, forecast) ->
                                if (currentWeather.isNotEmpty()) {
                                    dbDataSource.insertOrUpdateCurrentWeather(currentWeather)
                                }
                                if (forecast.isNotEmpty()) {
                                    dbDataSource.insertOrUpdateForecast(forecast)
                                }
                            }
                    }
            ) { }
    }

    //Openweather api has batch request for multiple cities current weather, but has no for forecast
    private fun fetchForecastForAll(cities: List<com.soramitsu.test.repository.model.db.City>)
            : Single<List<ApiForecastWeatherResponse>> =
        Single
            .zip(
                cities.map {
                    apiDataSource.fetchForecastWeather(cityId = it.id)
                }
            ) { listOf(*(it as Array<ApiForecastWeatherResponse>)) }

    private fun handleException(exception: Throwable) {
        val error = if (exception is NetworkErrorException) {
            NetworkConnectionError
        } else {
            RefreshDataError
        }

        errorBus.onNext(error)
    }

    override fun listenErrors(): Observable<Error> = errorBus.hide()

    override fun observeCitiesCurrentWeather(): Flowable<City> =
        dbDataSource
            .fetchCurrentWeather()
            .map { it.toDomainModel() }

    override fun refresh() = refreshTrigger.onNext(Trigger)
}