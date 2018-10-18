package com.soramitsu.test.repository.datasource.db

import com.soramitsu.test.repository.model.api.ApiCurrentWeatherResponse
import com.soramitsu.test.repository.model.api.ApiForecastWeatherResponse
import com.soramitsu.test.repository.model.db.City
import com.soramitsu.test.repository.model.db.CityWithWeather
import com.soramitsu.test.repository.model.db.WeatherForecast
import com.soramitsu.test.repository.toDatabaseModel
import io.reactivex.Flowable
import io.reactivex.Single

class DbDataSource(private val weatherDao: WeatherDao) {

    fun fetchCurrentWeather(): Flowable<CityWithWeather> =
        weatherDao
            .getCititesAndWeather()
            .map { cityWithWeather ->
                CityWithWeather().apply {
                    city = cityWithWeather.city
                    weather =
                            cityWithWeather.weather.filter { it.forDay == WeatherForecast.ForecastDay.CURRENT_WEATHER }
                }
            }

    fun fetchForecastWeatherForCity(cityName: String): Flowable<CityWithWeather> =
        weatherDao
            .getWeatherForCity(cityName)

    fun getCitiesInDb(): Single<List<City>> =
        weatherDao
            .getAllCities()

    fun insertOrUpdateCurrentWeather(currentWeather: List<ApiCurrentWeatherResponse>) =
        currentWeather
            .map { it.toDatabaseModel() }
            .forEach { weatherDao.insertOrUpdateWeather(it) }

    fun insertOrUpdateForecast(forecast: List<ApiForecastWeatherResponse>) =
        forecast
            .map { it.toDatabaseModel() }
            //we get 3 database weather models from one forecast response
            //3 days forecast
            .forEach { forecastsForCity ->
                forecastsForCity
                    .forEach { weatherDao.insertOrUpdateWeather(it) }
            }
}