package com.soramitsu.test.repository.datasource.api

import retrofit2.http.GET
import retrofit2.http.Query

const val CITY_NAME_QUERY_PARAM = "q"
const val CITY_ID_QUERY_PARAM = "id"
const val LANG_QUERY_PARAM = "lang"
const val UNITS_QUERY_PARAM = "units"
const val APP_KEY_QUERY_PARAM = "appid"

const val CURRENT_WEATHER_PATH = "weather"
const val FORECAST_WEATHER_PATH = "forecast"

interface WeatherApi {

    @GET(CURRENT_WEATHER_PATH)
    fun fetchCurrentWeatherForCityName(
        @Query(CITY_NAME_QUERY_PARAM) cityName: String,
        @Query(LANG_QUERY_PARAM) languageCode: String,
        @Query(UNITS_QUERY_PARAM) measureUnits: String,
        @Query(APP_KEY_QUERY_PARAM) appKey: String
    )

    @GET(FORECAST_WEATHER_PATH)
    fun fetchForecastWeatherForCityName(
        @Query(CITY_NAME_QUERY_PARAM) cityName: String,
        @Query(LANG_QUERY_PARAM) languageCode: String,
        @Query(UNITS_QUERY_PARAM) measureUnits: String,
        @Query(APP_KEY_QUERY_PARAM) appKey: String
    )

    @GET(CURRENT_WEATHER_PATH)
    fun fetchCurrentWeatherForCityId(
        @Query(CITY_ID_QUERY_PARAM) cityId: Long,
        @Query(LANG_QUERY_PARAM) languageCode: String,
        @Query(UNITS_QUERY_PARAM) measureUnits: String,
        @Query(APP_KEY_QUERY_PARAM) appKey: String
    )

    @GET(FORECAST_WEATHER_PATH)
    fun fetchForecastWeatherForCityId(
        @Query(CITY_ID_QUERY_PARAM) cityId: String,
        @Query(LANG_QUERY_PARAM) languageCode: String,
        @Query(UNITS_QUERY_PARAM) measureUnits: String,
        @Query(APP_KEY_QUERY_PARAM) appKey: String
    )
}