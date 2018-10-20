package com.soramitsu.test.domain.interfaces

import com.soramitsu.test.domain.models.City
import io.reactivex.Flowable

interface WeatherRepository {

    fun observeCitiesCurrentWeather(): Flowable<List<City>>

    fun refresh()
}