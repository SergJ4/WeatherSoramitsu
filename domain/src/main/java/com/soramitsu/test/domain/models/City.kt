package com.soramitsu.test.domain.models

data class City(
    val id: Long,
    val title: String,
    val weatherPrediction: List<Weather>
) {
    val currentWeather = weatherPrediction.find {
        it.day == Weather.WeatherDay.CURRENT_WEATHER
    }
}