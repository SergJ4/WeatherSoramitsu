package com.soramitsu.test.domain.interfaces


interface Logger {

    fun logErrorIfDebug(error: Throwable)

    fun logDebug(message: String, tag: String)
}