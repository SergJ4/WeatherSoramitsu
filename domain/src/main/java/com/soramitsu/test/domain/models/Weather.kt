package com.soramitsu.test.domain.models

data class Weather(
    val code: Int,
    val description: String,
    val dateTimestamp: Long,
    val temperatureMin: Float,
    val temperatureMax: Float,
    val humidity: Float,
    val windSpeed: Float,
    val icon: String
)