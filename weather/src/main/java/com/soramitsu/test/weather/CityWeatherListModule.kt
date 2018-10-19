package com.soramitsu.test.weather

import com.soramitsu.test.core.base.SwipeRefresh
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.singleton

fun cityWeatherListModule(kodein: Kodein) =
    Kodein.Module(CityWeatherListFragment::class.java.canonicalName) {
        bind<SwipeRefresh>() with singleton { SwipeRefresh() }
        bind<CityWeatherListPresenter>() with singleton { CityWeatherListPresenter(kodein) }
    }