package com.soramitsu.test.repository.model.api

import com.google.gson.annotations.SerializedName

data class ApiWeatherMainInfo(
    @SerializedName("temp")
    val temperature: Float,

    val pressure: Int,
    val humidity: Float
)