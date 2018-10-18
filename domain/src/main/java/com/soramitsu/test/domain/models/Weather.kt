package com.soramitsu.test.domain.models

import java.util.*

data class Weather(
    val code: Int,
    val description: String,
    val date: Date,
    val temperature: Float,
    val humidity: Float,
    val windSpeed: Float,
    val pressure: Int,
    val icon: String
)