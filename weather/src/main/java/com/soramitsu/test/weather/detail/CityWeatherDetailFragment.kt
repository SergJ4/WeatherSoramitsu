package com.soramitsu.test.weather.detail

import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.PresenterType
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.soramitsu.test.core.base.BaseFragment
import com.soramitsu.test.weather.R
import org.kodein.di.Kodein

class CityWeatherDetailFragment : BaseFragment() {

    @InjectPresenter
    lateinit var presenter: CityWeatherDetailPresenter

    @ProvidePresenter(type = PresenterType.LOCAL)
    fun presenterProvider() = providePresenter<CityWeatherDetailPresenter>()

    override val layoutRes: Int = R.layout.city_weather_detail_layout

    override fun module(): Kodein.Module = cityWeatherDetailModule(kodein)
}