package com.soramitsu.test.core.base

import com.arellomobile.mvp.MvpView

interface BaseView : MvpView {

    fun showProgress()

    fun hideProgress()

    fun showMessage(message: String)
}