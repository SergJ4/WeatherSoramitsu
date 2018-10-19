package com.soramitsu.test.weather.list

import com.arellomobile.mvp.InjectViewState
import com.soramitsu.test.core.base.BasePresenter
import com.soramitsu.test.domain.models.City
import com.soramitsu.test.weather.usecase.FetchCitiesWithWeather
import org.kodein.di.Kodein
import org.kodein.di.direct
import org.kodein.di.generic.instance

@InjectViewState
class CityWeatherListPresenter(kodein: Kodein) : BasePresenter<CityWeatherListView>(kodein) {

    private val fetchCitiesWithWeather: FetchCitiesWithWeather by instance()

    init {
        executor
            .executeAsync(fetchCitiesWithWeather().map { createItem(it) }) {
                viewState.showCity(it)
            }
    }

    private fun createItem(city: City): CityWeatherItem =
        CityWeatherItem(imageLoader = kodein.direct.instance(), city = city)
}