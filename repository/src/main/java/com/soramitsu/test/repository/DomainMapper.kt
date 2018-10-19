package com.soramitsu.test.repository

import com.soramitsu.test.domain.models.City
import com.soramitsu.test.domain.models.Weather
import com.soramitsu.test.repository.model.db.CityWithWeather
import com.soramitsu.test.repository.model.db.WeatherForecast

fun CityWithWeather.toDomainModel(): City =
    City(
        id = city?.id ?: throw IllegalStateException("City id is null: ${toString()}"),
        title = city?.name ?: throw IllegalStateException("City name is null: ${toString()}"),
        weatherPrediction = weather.map { it.toDomainModel() }
    )

fun WeatherForecast.toDomainModel(): Weather =
    Weather(
        code = code,
        description = description,
        day = forDay.toDomainModel(),
        temperature = temperature,
        humidity = humidity,
        windSpeed = windSpeed,
        pressure = pressure,
        icon = "$WEATHER_ICON_URL$icon.png"
    )

fun WeatherForecast.ForecastDay.toDomainModel(): Weather.WeatherDay =
    when (this) {
        WeatherForecast.ForecastDay.CURRENT_WEATHER -> Weather.WeatherDay.CURRENT_WEATHER
        WeatherForecast.ForecastDay.TOMORROW -> Weather.WeatherDay.TOMORROW
        WeatherForecast.ForecastDay.AFTER_TOMORROW -> Weather.WeatherDay.AFTER_TOMORROW
        WeatherForecast.ForecastDay.THIRD_DAY -> Weather.WeatherDay.THIRD_DAY
    }