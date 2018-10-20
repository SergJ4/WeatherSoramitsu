package com.soramitsu.test.weather.detail

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.PresenterType
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.soramitsu.test.core.base.BaseFragment
import com.soramitsu.test.weather.R
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.IFlexible
import kotlinx.android.synthetic.main.city_weather_detail_layout.*
import org.kodein.di.Kodein

internal const val CITY_ID_ARG = "city id"

class CityWeatherDetailFragment : BaseFragment(), CityWeatherDetailView {

    private val adapter: FlexibleAdapter<IFlexible<*>> = FlexibleAdapter(null, null, true)

    @InjectPresenter
    lateinit var presenter: CityWeatherDetailPresenter

    @ProvidePresenter(type = PresenterType.LOCAL)
    fun presenterProvider() = providePresenter<CityWeatherDetailPresenter>()

    override val layoutRes: Int = R.layout.city_weather_detail_layout

    override fun module(): Kodein.Module = cityWeatherDetailModule(
        kodein, arguments!!.getLong(CITY_ID_ARG)
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (weatherList.adapter == null) {
            weatherList.adapter = adapter
        }

        if (weatherList.layoutManager == null) {
            weatherList.layoutManager = LinearLayoutManager(weatherList.context)
        }

        swipeRefresh.setOnRefreshListener(presenter.swipeRefresh)
        backButton.setOnClickListener { presenter.back() }
    }

    override fun showCity(items: List<IFlexible<*>>) = adapter.updateDataSet(items)

    companion object {
        fun getInstance(cityId: Long): CityWeatherDetailFragment {
            val fragment = CityWeatherDetailFragment()
            val args = Bundle()
            args.putLong(CITY_ID_ARG, cityId)
            fragment.arguments = args
            return fragment
        }
    }
}