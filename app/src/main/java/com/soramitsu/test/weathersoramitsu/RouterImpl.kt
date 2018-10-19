package com.soramitsu.test.weathersoramitsu

import android.support.v4.app.Fragment
import com.soramitsu.test.domain.interfaces.Router
import com.soramitsu.test.domain.interfaces.WEATHER_LIST_SCREEN
import com.soramitsu.test.weather.list.CityWeatherListFragment
import ru.terrakok.cicerone.Screen
import ru.terrakok.cicerone.android.support.SupportAppScreen

class RouterImpl(private val router: ru.terrakok.cicerone.Router) : Router {

    override fun goTo(screenName: String, data: Any?) =
        router.navigateTo(convertNameToScreen(screenName, data))

    override fun rootScreen(screenName: String) =
        router.newRootScreen(convertNameToScreen(screenName, null))

    override fun back() = router.exit()

    private fun convertNameToScreen(screenName: String, data: Any?): Screen =
        when (screenName) {
            WEATHER_LIST_SCREEN -> WeatherListScreen()
            else -> throw IllegalArgumentException("unknown screen name: $screenName")
        }
}

internal class WeatherListScreen() : SupportAppScreen() {

    override fun getFragment(): Fragment = CityWeatherListFragment()
}