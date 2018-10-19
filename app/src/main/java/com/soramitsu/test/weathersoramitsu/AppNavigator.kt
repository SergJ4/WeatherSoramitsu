package com.soramitsu.test.weathersoramitsu

import android.support.v4.app.FragmentManager
import ru.terrakok.cicerone.android.support.SupportAppNavigator

class AppNavigator(activity: MainActivity, fragmentManager: FragmentManager, containerId: Int) :
    SupportAppNavigator(activity, fragmentManager, containerId)