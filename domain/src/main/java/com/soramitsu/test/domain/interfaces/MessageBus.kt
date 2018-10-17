package com.soramitsu.test.domain.interfaces

import io.reactivex.Observable

interface MessageBus {

    fun showMessage(message: String = "", resId: Int = -1)

    fun listen(): Observable<String>
}