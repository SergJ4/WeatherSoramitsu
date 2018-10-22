package com.soramitsu.test.weather.list

import com.soramitsu.test.core.base.SwipeRefresh
import com.soramitsu.test.core.usecase.RefreshWeatherRepo
import com.soramitsu.test.weather.usecase.AddCity
import com.soramitsu.test.weather.usecase.FetchCitiesWithWeather
import com.soramitsu.test.weather.usecase.GooglePlaces
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton

fun cityWeatherListModule(
    kodein: Kodein
) =
    Kodein.Module(CityWeatherListFragment::class.java.canonicalName) {
        bind<SwipeRefresh>() with singleton { SwipeRefresh() }
        bind<CityWeatherListPresenter>() with singleton {
            CityWeatherListPresenter(
                kodein
            )
        }
        bind<FetchCitiesWithWeather>() with singleton { FetchCitiesWithWeather(instance()) }
        bind<RefreshWeatherRepo>() with singleton {
            RefreshWeatherRepo(
                instance(),
                instance(),
                instance()
            )
        }
        bind<GooglePlaces>() with singleton {
            GooglePlaces(
                instance(),
                instance(),
                instance()
            )
        }
        bind<AddCity>() with singleton { AddCity(instance(), instance(), instance()) }
    }