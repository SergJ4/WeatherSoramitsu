package com.soramitsu.test.repository

import com.soramitsu.test.domain.SchedulersProvider
import com.soramitsu.test.domain.exceptions.*
import com.soramitsu.test.domain.interfaces.ApiErrors
import com.soramitsu.test.domain.interfaces.Logger
import com.soramitsu.test.domain.interfaces.WeatherRepository
import com.soramitsu.test.domain.models.City
import com.soramitsu.test.repository.datasource.api.ApiDataSource
import com.soramitsu.test.repository.datasource.db.DbDataSource
import com.soramitsu.test.repository.model.api.ApiForecastWeatherResponse
import com.soramitsu.test.repository.model.api.ApiGroupedWeatherResponse
import com.soramitsu.test.repository.model.api.currentWeatherError
import com.soramitsu.test.repository.model.api.forecastWeatherError
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.BiFunction

class WeatherRepository(
    private val apiDataSource: ApiDataSource,
    private val dbDataSource: DbDataSource,
    compositeDisposable: CompositeDisposable,
    private val apiErrors: ApiErrors,
    private val logger: Logger
) : WeatherRepository {

    init {
        compositeDisposable
            .add(
                refresh()
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

    override fun observeCitiesCurrentWeather(): Flowable<List<City>> =
        dbDataSource
            .fetchCurrentWeather()
            .map { list -> list.map { it.toDomainModel() } }

    override fun observeExactCityWeather(cityId: Long): Flowable<City> =
        dbDataSource
            .fetchForecastWeatherForCityId(cityId)
            .map {
                it.toDomainModel()
            }

    override fun addCity(cityName: String): Completable =
        apiDataSource
            .fetchCurrentWeather(cityName)
            .onErrorReturn {
                handleException(it)
                currentWeatherError
            }
            .filter { it != currentWeatherError }
            .doOnSuccess {
                dbDataSource.addCity(
                    com.soramitsu.test.repository.model.db.City(
                        it.cityId,
                        it.cityName
                    )
                )
                dbDataSource.insertOrUpdateCurrentWeather(listOf(it))
            }
            .flatMap { currentWeatherResponse ->
                apiDataSource
                    .fetchForecastWeather(cityId = currentWeatherResponse.cityId)
                    .onErrorReturn {
                        handleException(it)
                        forecastWeatherError
                    }
                    .filter { it != forecastWeatherError }
                    .doOnSuccess {
                        dbDataSource.insertOrUpdateForecast(listOf(it))
                    }
            }
            .ignoreElement()

    override fun refresh(): Completable = dbDataSource
        .getCitiesInDb()
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
        .ignoreElement()

    private fun handleException(exception: Throwable) {
        logger.logErrorIfDebug(exception)

        val error = when {
            exception is NetworkErrorException && exception.code == NOT_FOUND -> ResourceNotFoundError
            exception is NetworkErrorException -> NetworkConnectionError
            else -> RefreshDataError
        }

        apiErrors(error)
    }
}