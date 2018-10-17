package com.soramitsu.test.domain.interfaces

interface Router {

    fun goTo(screenName: String, data: Any? = null)

    fun rootScreen(screenName: String)

    fun back()
}