package com.soramitsu.test.weather

import android.view.View
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.viewholders.FlexibleViewHolder
import kotlinx.android.extensions.LayoutContainer

class CityWeatherViewHolder(view: View, adapter: FlexibleAdapter<*>) :
    FlexibleViewHolder(view, adapter), LayoutContainer {

    override val containerView: View = view

    fun bind(cityWeatherItem: CityWeatherItem) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}