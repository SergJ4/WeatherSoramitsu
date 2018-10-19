package com.soramitsu.test.weathersoramitsu

import android.os.Bundle
import com.arellomobile.mvp.InjectViewState
import com.soramitsu.test.core.base.BasePresenter
import com.soramitsu.test.domain.interfaces.WEATHER_LIST_SCREEN
import org.kodein.di.Kodein

@InjectViewState
class MainPresenter(kodein: Kodein) : BasePresenter<MainView>(kodein) {

    fun handleNavigation(savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            router.rootScreen(WEATHER_LIST_SCREEN)
        }
    }
}
