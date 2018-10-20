package com.soramitsu.test.domain.interfaces

const val WEATHER_LIST_SCREEN = "weather_list"
const val WEATHER_DETAILS_SCREEN = "weather_details"

interface Router {

    fun goTo(screenName: String, data: Any? = null)

    fun rootScreen(screenName: String)

    fun back()
}