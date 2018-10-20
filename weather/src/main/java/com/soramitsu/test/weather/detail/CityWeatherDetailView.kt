package com.soramitsu.test.weather.detail

import com.soramitsu.test.core.base.BaseView
import eu.davidea.flexibleadapter.items.IFlexible

interface CityWeatherDetailView : BaseView {

    fun showCity(items: List<IFlexible<*>>)
}