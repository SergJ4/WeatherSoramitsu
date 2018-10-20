package com.soramitsu.test.weather.detail

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.soramitsu.test.core.base.BaseView
import eu.davidea.flexibleadapter.items.IFlexible

interface CityWeatherDetailView : BaseView {

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun showCity(items: List<IFlexible<*>>)
}