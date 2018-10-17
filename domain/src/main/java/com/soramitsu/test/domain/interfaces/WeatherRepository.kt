package com.soramitsu.test.domain.interfaces

import com.soramitsu.test.domain.models.City
import io.reactivex.Observable

interface WeatherRepository {

    fun observeCity(cityId: String): Observable<City>
}