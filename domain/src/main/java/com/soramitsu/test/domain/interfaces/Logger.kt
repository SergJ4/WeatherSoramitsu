package com.soramitsu.test.domain.interfaces


interface Logger {

    fun d(message: String)

    fun d(message: String, tag: String)

    fun e(message: String)

    fun e(throwable: Throwable)

    fun init()
}