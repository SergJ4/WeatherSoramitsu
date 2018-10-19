package com.soramitsu.test.repository

import com.soramitsu.test.domain.SchedulersProvider
import com.soramitsu.test.domain.exceptions.NetworkConnectionError
import com.soramitsu.test.domain.exceptions.NetworkErrorException
import com.soramitsu.test.domain.exceptions.RefreshDataError
import com.soramitsu.test.domain.interfaces.ApiErrors
import com.soramitsu.test.domain.interfaces.Logger
import com.soramitsu.test.domain.interfaces.WeatherRepository
import com.soramitsu.test.domain.models.City
import com.soramitsu.test.domain.models.Trigger
import com.soramitsu.test.repository.datasource.api.ApiDataSource
import com.soramitsu.test.repository.datasource.db.DbDataSource
import com.soramitsu.test.repository.model.api.ApiForecastWeatherResponse
import com.soramitsu.test.repository.model.api.ApiGroupedWeatherResponse
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.BiFunction
import io.reactivex.subjects.PublishSubject

class WeatherRepository(
    private val apiDataSource: ApiDataSource,
    private val dbDataSource: DbDataSource,
    compositeDisposable: CompositeDisposable,
    private val apiErrors: ApiErrors,
    private val logger: Logger
) : WeatherRepository {

    private val refreshTrigger: PublishSubject<Trigger> = PublishSubject.create()

    init {
        compositeDisposable
            .add(
                refreshTrigger
                    .startWith(Trigger)
                    .flatMapSingle { dbDataSource.getCitiesInDb() }
                    .filter { it.isNotEmpty() }
                    .flatMapSingle { cities ->
                        apiDataSource
                            .fetchCurrentWeatherForAll(cities)
                            .onErrorReturn {
                                handleException(it)
                                ApiGroupedWeatherResponse(listOf())
                            }
                            .zipWith(
                                fetchForecastForAll(cities).onErrorReturn {
                                    handleException(it)
                                    listOf()
                                },
                                BiFunction { currentWeatherList: ApiGroupedWeatherResponse,
                                             forecastList: List<ApiForecastWeatherResponse> ->
                                    currentWeatherList to forecastList
                                }
                            )
                            .doOnSuccess { (currentWeather, forecast) ->
                                if (currentWeather.weatherList.isNotEmpty()) {
                                    dbDataSource.insertOrUpdateCurrentWeather(currentWeather.weatherList)
                                }
                                if (forecast.isNotEmpty()) {
                                    dbDataSource.insertOrUpdateForecast(forecast)
                                }
                            }
                    }
                    .subscribeOn(SchedulersProvider.io())
                    .subscribe()
            )
    }

    //Openweather api has batch request for multiple cities current weather, but has no for forecast
    private fun fetchForecastForAll(cities: List<com.soramitsu.test.repository.model.db.City>)
            : Single<List<ApiForecastWeatherResponse>> =
        Single
            .zip(
                cities.map {
                    apiDataSource.fetchForecastWeather(cityId = it.id)
                }
            ) { array ->
                array.map { it as ApiForecastWeatherResponse }.toList()
            }

    private fun handleException(exception: Throwable) {
        logger.logErrorIfDebug(exception)

        val error = if (exception is NetworkErrorException) {
            NetworkConnectionError
        } else {
            RefreshDataError
        }

        apiErrors(error)
    }

    override fun observeCitiesCurrentWeather(): Flowable<City> =
        dbDataSource
            .fetchCurrentWeather()
            .map { it.toDomainModel() }

    override fun refresh() = refreshTrigger.onNext(Trigger)
}