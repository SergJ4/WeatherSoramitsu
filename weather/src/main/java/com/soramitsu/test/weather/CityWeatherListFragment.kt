package com.soramitsu.test.weather

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.soramitsu.test.core.base.BaseFragment
import com.soramitsu.test.core.base.SwipeRefresh
import eu.davidea.flexibleadapter.FlexibleAdapter
import kotlinx.android.synthetic.main.city_weather_list_layout.*
import org.kodein.di.Kodein
import org.kodein.di.generic.instance

class CityWeatherListFragment : BaseFragment(), CityWeatherListView {

    @InjectPresenter
    lateinit var presenter: CityWeatherListPresenter

    @ProvidePresenter
    fun presenterProvider() = providePresenter<CityWeatherListPresenter>()

    override val layoutRes: Int = R.layout.city_weather_list_layout

    private val adapter: FlexibleAdapter<CityWeatherItem> =
        FlexibleAdapter(null, null, true)

    override fun module(): Kodein.Module = cityWeatherListModule(kodein)

    private val swipeRefreshListener: SwipeRefresh by instance()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        swipeRefresh.setOnRefreshListener(swipeRefreshListener)
    }

    override fun showCities(cities: List<CityWeatherItem>) {
        if (cityList.adapter == null) {
            cityList.adapter = adapter
        }

        if (cityList.layoutManager == null) {
            cityList.layoutManager = LinearLayoutManager(cityList.context)
        }

        adapter.updateDataSet(cities)
    }
}