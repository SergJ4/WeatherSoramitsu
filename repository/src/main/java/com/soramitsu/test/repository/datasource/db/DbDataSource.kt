package com.soramitsu.test.repository.datasource.db

import com.soramitsu.test.repository.model.db.CityWithWeather
import com.soramitsu.test.repository.model.db.WeatherForecast
import io.reactivex.Flowable

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

}