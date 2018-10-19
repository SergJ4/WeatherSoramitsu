package com.soramitsu.test.weather.list

import com.soramitsu.test.core.base.BaseView

interface CityWeatherListView : BaseView {

    fun showCity(city: CityWeatherItem)
}