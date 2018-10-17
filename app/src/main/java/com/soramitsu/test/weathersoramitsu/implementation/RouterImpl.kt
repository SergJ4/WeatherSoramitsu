package com.soramitsu.test.weathersoramitsu.implementation

import com.soramitsu.test.domain.interfaces.Router
import ru.terrakok.cicerone.Screen

class RouterImpl(private val router: ru.terrakok.cicerone.Router) : Router {

    override fun goTo(screenName: String, data: Any?) =
        router.navigateTo(convertNameToScreen(screenName, data))

    override fun rootScreen(screenName: String) =
        router.newRootScreen(convertNameToScreen(screenName, null))

    override fun back() = router.exit()

    private fun convertNameToScreen(screenName: String, data: Any?): Screen =
        when (screenName) {
            else -> throw IllegalArgumentException("unknown screen name: $screenName")
        }
}