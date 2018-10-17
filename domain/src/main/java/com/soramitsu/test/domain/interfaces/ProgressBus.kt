package com.soramitsu.test.domain.interfaces

import android.view.View


interface ProgressBus {

    fun showProgress(shouldShow: Boolean)

    fun attachView(view: View?)
}