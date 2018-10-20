package com.soramitsu.test.weather.detail.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import com.soramitsu.test.domain.interfaces.ImageLoader
import com.soramitsu.test.domain.models.City
import com.soramitsu.test.weather.R
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem
import eu.davidea.flexibleadapter.items.IFlexible

class CurrentDetailsItem(
    private val city: City,
    internal val imageLoader: ImageLoader,
    private val context: Context
) : AbstractFlexibleItem<CurrentDetailsViewHolder>() {

    internal val cityName: String = city.title
    internal val weatherIcon: String = city.currentWeather?.icon ?: ""
    internal val weatherDescription: String = city.currentWeather?.description ?: ""
    internal val temperature: String
        get() {
            val temp = city.currentWeather?.temperature?.toInt() ?: 0
            val result = "$temp\u2103"
            return if (temp > 0) {
                "+$result"
            } else {
                result
            }
        }
    internal val pressure: String
        get() {
            return "${city.currentWeather?.pressure?.toInt()} ${context.getString(R.string.pressure_units)}"
        }
    internal val humidity: String
        get() {
            return "${city.currentWeather?.humidity}%"
        }
    internal val wind: String
        get() {
            return "${city.currentWeather?.windSpeed} ${context.getString(R.string.speed_units)}"
        }

    override fun bindViewHolder(
        adapter: FlexibleAdapter<IFlexible<RecyclerView.ViewHolder>>,
        holder: CurrentDetailsViewHolder,
        position: Int,
        payloads: MutableList<Any>?
    ) = holder.bind(this)

    override fun equals(other: Any?): Boolean =
        other is CurrentDetailsItem && city == other.city

    override fun hashCode(): Int = city.hashCode()

    override fun createViewHolder(
        view: View,
        adapter: FlexibleAdapter<IFlexible<RecyclerView.ViewHolder>>
    ): CurrentDetailsViewHolder = CurrentDetailsViewHolder(view, adapter)

    override fun getLayoutRes(): Int = R.layout.current_weather_detail_item
}