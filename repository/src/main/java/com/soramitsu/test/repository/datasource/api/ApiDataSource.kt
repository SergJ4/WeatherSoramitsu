package com.soramitsu.test.repository.datasource.api

import android.content.Context
import com.soramitsu.test.repository.R
import com.soramitsu.test.repository.model.api.ApiCurrentWeatherResponse
import com.soramitsu.test.repository.model.api.ApiForecastWeatherResponse
import io.reactivex.Single
import java.util.*

const val DEFAULT_MEASURE_UNITS = "metric"

class ApiDataSource(private val weatherApi: WeatherApi, context: Context) {

    private val apiKey = context.getString(R.string.app_key)

    fun fetchCurrentWeather(cityName: String): Single<ApiCurrentWeatherResponse> =
        weatherApi.fetchCurrentWeatherForCityName(
            cityName,
            defineLanguage(),
            DEFAULT_MEASURE_UNITS,
            apiKey
        )

    fun fetchForecastWeather(cityName: String): Single<ApiForecastWeatherResponse> =
        weatherApi.fetchForecastWeatherForCityName(
            cityName,
            defineLanguage(),
            DEFAULT_MEASURE_UNITS,
            apiKey
        )

    private fun defineLanguage(): String {
        val language = Locale.getDefault().language

        return if (language != "ru") {
            "en"
        } else {
            language
        }
    }
}