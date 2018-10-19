package com.soramitsu.test.weathersoramitsu

import com.arellomobile.mvp.InjectViewState
import com.soramitsu.test.domain.base.BasePresenter
import org.kodein.di.Kodein

@InjectViewState
class MainPresenter(kodein: Kodein) : BasePresenter<MainView>(kodein) {
}
