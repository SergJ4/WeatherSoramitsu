package com.soramitsu.test.weather.list

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.soramitsu.test.core.base.BaseView
import com.soramitsu.test.weather.list.adapter.CityWeatherItem

interface CityWeatherListView : BaseView {

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun showCities(cities: List<CityWeatherItem>)
}