package com.soramitsu.test.weather.list

import com.arellomobile.mvp.InjectViewState
import com.soramitsu.test.core.base.BasePresenter
import com.soramitsu.test.core.base.SwipeRefresh
import com.soramitsu.test.core.usecase.RefreshWeatherRepo
import com.soramitsu.test.domain.models.City
import com.soramitsu.test.weather.usecase.FetchCitiesWithWeather
import org.kodein.di.Kodein
import org.kodein.di.direct
import org.kodein.di.generic.instance

@InjectViewState
class CityWeatherListPresenter(kodein: Kodein) : BasePresenter<CityWeatherListView>(kodein) {

    private val fetchCitiesWithWeather: FetchCitiesWithWeather by instance()
    private val refreshWeatherRepo: RefreshWeatherRepo by instance()
    internal val swipeRefreshListener: SwipeRefresh by instance()

    init {
        viewState.showProgress()
        executor
            .executeAsync(
                fetchCitiesWithWeather()
                    .map {
                        createItems(it)
                    }) {
                viewState.hideProgress()
                viewState.showCities(it)
            }

        executor
            .executeAsync(refreshWeatherRepo())
    }

    private fun createItems(cities: List<City>): List<CityWeatherItem> =
        cities
            .map {
                CityWeatherItem(
                    imageLoader = kodein.direct.instance(),
                    city = it,
                    context = kodein.direct.instance()
                )
            }
}