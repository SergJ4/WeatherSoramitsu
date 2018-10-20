package com.soramitsu.test.weather.detail

import android.content.Context
import com.arellomobile.mvp.InjectViewState
import com.soramitsu.test.core.base.BasePresenter
import com.soramitsu.test.core.base.SwipeRefresh
import com.soramitsu.test.core.usecase.RefreshWeatherRepo
import com.soramitsu.test.domain.interfaces.ImageLoader
import com.soramitsu.test.domain.models.City
import com.soramitsu.test.domain.models.Weather
import com.soramitsu.test.weather.detail.adapter.CurrentDetailsItem
import com.soramitsu.test.weather.detail.adapter.ForecastDetailsItem
import com.soramitsu.test.weather.usecase.FetchExactCityWeather
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem
import org.kodein.di.Kodein
import org.kodein.di.direct
import org.kodein.di.generic.instance

@InjectViewState
class CityWeatherDetailPresenter(kodein: Kodein) : BasePresenter<CityWeatherDetailView>(kodein) {

    private val cityId: Long by instance(tag = CITY_ID_ARG)
    private val fetchExactCityWeather: FetchExactCityWeather by instance()
    private val refreshWeatherRepo: RefreshWeatherRepo by instance()
    internal val swipeRefresh: SwipeRefresh by instance()

    init {
        viewState.showProgress()
        executor
            .executeAsync(fetchExactCityWeather(cityId).map { createAdapterItems(it) }) {
                viewState.hideProgress()
                viewState.showCity(it)
            }

        executor
            .executeAsync(refreshWeatherRepo())
    }

    private fun createAdapterItems(city: City): List<AbstractFlexibleItem<*>> {
        val result = mutableListOf<AbstractFlexibleItem<*>>()

        val imageLoader = kodein.direct.instance<ImageLoader>()
        val context = kodein.direct.instance<Context>()

        result.add(CurrentDetailsItem(city, imageLoader, context))

        val forecastItems = city
            .weatherPrediction
            .asSequence()
            .filter {
                it.day != Weather.WeatherDay.CURRENT_WEATHER
            }
            .map {
                ForecastDetailsItem(it, imageLoader, context)
            }
            .toList()

        result.addAll(forecastItems)
        return result
    }

    fun back() = router.back()
}