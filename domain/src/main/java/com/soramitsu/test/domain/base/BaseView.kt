package com.soramitsu.test.domain.base

import com.arellomobile.mvp.MvpView

interface BaseView : MvpView {

    fun showProgress()

    fun hideProgress()

    fun showMessage(message: String)
}