package com.soramitsu.test.repository.model.api

import com.google.gson.annotations.SerializedName

data class ApiForecastWeatherResponse(
    val city: ApiCity,

    @SerializedName("list")
    val forecasts: List<ApiForecastWeather>
)