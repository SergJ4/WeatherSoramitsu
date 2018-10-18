package com.soramitsu.test.repository.model.db

import android.arch.persistence.room.Embedded
import android.arch.persistence.room.Relation

class CityWithWeather {

    @Embedded
    var city: City? = null

    @Relation(parentColumn = CITY_ID_COLUMN, entityColumn = FORECAST_CITY_ID_COLUMN)
    var weather: List<WeatherForecast> = listOf()
}