package com.soramitsu.test.weathersoramitsu

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import ru.terrakok.cicerone.android.support.SupportAppNavigator
import ru.terrakok.cicerone.android.support.SupportAppScreen

class AppNavigator(activity: MainActivity, fragmentManager: FragmentManager, containerId: Int) :
    SupportAppNavigator(activity, fragmentManager, containerId) {

    override fun createFragment(screen: SupportAppScreen?): Fragment {
        TODO()
    }
}