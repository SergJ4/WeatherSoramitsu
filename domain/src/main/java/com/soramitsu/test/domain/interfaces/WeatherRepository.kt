package com.soramitsu.test.domain.interfaces

import com.soramitsu.test.domain.exceptions.Error
import com.soramitsu.test.domain.models.City
import io.reactivex.Flowable
import io.reactivex.Observable

interface WeatherRepository {

    fun observeCitiesCurrentWeather(): Flowable<City>

    fun listenErrors(): Observable<Error>

    fun refresh()
}