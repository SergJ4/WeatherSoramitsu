package com.soramitsu.test.domain.interfaces

const val WEATHER_LIST_SCREEN = "weather_list"

interface Router {

    fun goTo(screenName: String, data: Any? = null)

    fun rootScreen(screenName: String)

    fun back()
}