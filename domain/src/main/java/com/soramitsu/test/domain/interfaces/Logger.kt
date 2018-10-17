package com.soramitsu.test.domain.interfaces


interface Logger {

    fun logErrorIfDebug(error: Throwable)
}