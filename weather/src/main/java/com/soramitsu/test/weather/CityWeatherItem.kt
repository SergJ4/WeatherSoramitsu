package com.soramitsu.test.weather

import android.support.v7.widget.RecyclerView
import android.view.View
import com.soramitsu.test.domain.models.City
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem
import eu.davidea.flexibleadapter.items.IFlexible

class CityWeatherItem(private val city: City) : AbstractFlexibleItem<CityWeatherViewHolder>() {

    override fun bindViewHolder(
        adapter: FlexibleAdapter<IFlexible<RecyclerView.ViewHolder>>,
        holder: CityWeatherViewHolder,
        position: Int,
        payloads: MutableList<Any>?
    ) = holder.bind(this)

    override fun equals(other: Any?): Boolean = other is CityWeatherItem &&
            city == other.city

    override fun hashCode(): Int = city.hashCode()

    override fun createViewHolder(
        view: View,
        adapter: FlexibleAdapter<IFlexible<RecyclerView.ViewHolder>>
    ): CityWeatherViewHolder = CityWeatherViewHolder(view, adapter)

    override fun getLayoutRes(): Int = R.layout.city_weather_item
}