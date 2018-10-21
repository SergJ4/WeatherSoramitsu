package com.soramitsu.test.domain.interfaces

import com.soramitsu.test.domain.models.City
import io.reactivex.Completable
import io.reactivex.Flowable

interface WeatherRepository {

    fun observeCitiesCurrentWeather(): Flowable<List<City>>

    fun observeExactCityWeather(cityId: Long): Flowable<City>

    fun addCity(cityName: String): Completable

    fun refresh(): Completable
}