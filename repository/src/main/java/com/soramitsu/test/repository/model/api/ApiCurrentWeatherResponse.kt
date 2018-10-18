package com.soramitsu.test.repository.model.api

import com.google.gson.annotations.SerializedName

data class ApiCurrentWeatherResponse(
    @SerializedName("main")
    val mainInfo: ApiWeatherMainInfo,

    @SerializedName("weather")
    val weatherDescription: List<ApiWeatherAdditionalInfo>,

    val wind: ApiWind,

    @SerializedName("id")
    val cityId: Long,

    @SerializedName("name")
    val cityName: String
)