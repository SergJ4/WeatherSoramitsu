package com.soramitsu.test.weather.list

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.soramitsu.test.core.base.BaseFragment
import com.soramitsu.test.weather.R
import com.soramitsu.test.weather.list.adapter.CityWeatherItem
import eu.davidea.flexibleadapter.FlexibleAdapter
import kotlinx.android.synthetic.main.city_weather_list_layout.*
import org.kodein.di.Kodein

class CityWeatherListFragment : BaseFragment(),
    CityWeatherListView {

    @InjectPresenter
    lateinit var presenter: CityWeatherListPresenter

    @ProvidePresenter
    fun presenterProvider() = providePresenter<CityWeatherListPresenter>()

    override val layoutRes: Int = R.layout.city_weather_list_layout

    private val adapter: FlexibleAdapter<CityWeatherItem> =
        FlexibleAdapter(null, null, true)

    override fun module(): Kodein.Module =
        cityWeatherListModule(kodein)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        swipeRefresh.setOnRefreshListener(presenter.swipeRefreshListener)

        if (cityList.adapter == null) {
            cityList.adapter = adapter
        }

        addCityButton.setOnClickListener { presenter.googlePlaces.showCitiesList(this) }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (!presenter.googlePlaces.onActivityResult(requestCode, resultCode, data, this)) {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun showCities(cities: List<CityWeatherItem>) = adapter.updateDataSet(cities)
}