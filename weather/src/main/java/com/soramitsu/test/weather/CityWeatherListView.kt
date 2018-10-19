package com.soramitsu.test.weather

import com.soramitsu.test.core.base.BaseView

interface CityWeatherListView : BaseView {

    fun showCities(cities: List<CityWeatherItem>)
}