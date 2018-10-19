package com.soramitsu.test.weathersoramitsu

import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.soramitsu.test.domain.base.BaseActivity
import org.kodein.di.Kodein
import org.kodein.di.generic.instance
import ru.terrakok.cicerone.NavigatorHolder

class MainActivity : BaseActivity(), MainView {

    override val layoutRes: Int = R.layout.main_activity_layout

    @InjectPresenter
    lateinit var presenter: MainPresenter

    private val navigator = AppNavigator(this, supportFragmentManager, R.id.fragmentsContainer)

    private val navigatorHolder: NavigatorHolder by instance()

    override fun module(): Kodein.Module = Kodein.Module(MainActivity::class.java.canonicalName) {}

    @ProvidePresenter
    fun presenterProvider() = providePresenter<MainPresenter>()

    override fun onResume() {
        super.onResume()

        navigatorHolder.setNavigator(navigator)
    }

    override fun onPause() {
        super.onPause()

        navigatorHolder.removeNavigator()
    }
}