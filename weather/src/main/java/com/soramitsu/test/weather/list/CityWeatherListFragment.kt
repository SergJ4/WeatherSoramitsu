package com.soramitsu.test.weather.list

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.PresenterType
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

    @ProvidePresenter(type = PresenterType.LOCAL)
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

        if (cityList.layoutManager == null) {
            cityList.layoutManager = LinearLayoutManager(cityList.context)
        }
    }

    override fun showCities(cities: List<CityWeatherItem>) = adapter.updateDataSet(cities)
}