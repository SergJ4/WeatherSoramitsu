package com.soramitsu.test.domain.interfaces

import io.reactivex.Observable


interface ProgressBus {

    fun show()

    fun hide()

    fun listen(): Observable<Boolean>
}