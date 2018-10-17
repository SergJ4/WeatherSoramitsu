package com.soramitsu.test.domain.interfaces

import android.view.View


interface MessageBus {

    var anchorView: View?

    fun showMessage(message: String = "", resId: Int = -1)

    fun showMessage(exception: Throwable)
}