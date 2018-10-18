package com.soramitsu.test.repository

import com.soramitsu.test.domain.interfaces.WeatherRepository
import com.soramitsu.test.domain.models.City
import com.soramitsu.test.repository.datasource.api.ApiDataSource
import com.soramitsu.test.repository.datasource.db.DbDataSource
import io.reactivex.Observable

class WeatherRepository(
    private val apiDataSource: ApiDataSource,
    private val dbDataSource: DbDataSource
) : WeatherRepository {

    override fun observeCity(cityId: String): Observable<City> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}