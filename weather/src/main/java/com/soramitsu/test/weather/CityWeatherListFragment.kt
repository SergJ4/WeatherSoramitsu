package com.soramitsu.test.weather

import com.soramitsu.test.domain.base.BaseFragment
import org.kodein.di.Kodein

class CityWeatherListFragment : BaseFragment() {

    override val layoutRes: Int
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.

    override fun module(): Kodein.Module = cityWeatherListModule()
}