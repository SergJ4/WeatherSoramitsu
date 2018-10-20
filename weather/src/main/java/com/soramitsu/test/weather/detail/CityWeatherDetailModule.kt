package com.soramitsu.test.weather.detail

import com.soramitsu.test.core.base.SwipeRefresh
import com.soramitsu.test.weather.usecase.FetchExactCityWeather
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton

fun cityWeatherDetailModule(kodein: Kodein, cityId: Long) =
    Kodein.Module(CityWeatherDetailFragment::class.java.canonicalName) {
        bind<SwipeRefresh>() with singleton { SwipeRefresh() }
        bind<CityWeatherDetailPresenter>() with singleton {
            CityWeatherDetailPresenter(
                kodein
            )
        }
        bind<FetchExactCityWeather>() with singleton { FetchExactCityWeather(instance()) }
        bind<Long>(tag = CITY_ID_ARG) with instance(cityId)
    }