package com.soramitsu.test.weather

import com.arellomobile.mvp.InjectViewState
import com.soramitsu.test.core.base.BasePresenter
import org.kodein.di.Kodein

@InjectViewState
class CityWeatherListPresenter(kodein: Kodein) : BasePresenter<CityWeatherListView>(kodein) {
}