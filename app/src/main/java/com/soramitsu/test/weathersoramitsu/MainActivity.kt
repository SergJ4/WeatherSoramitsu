package com.soramitsu.test.weathersoramitsu

import android.os.Bundle
import android.os.PersistableBundle
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.soramitsu.test.core.base.BaseActivity
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton
import ru.terrakok.cicerone.NavigatorHolder

class MainActivity : BaseActivity(), MainView {

    override val layoutRes: Int = R.layout.main_activity_layout

    @InjectPresenter
    lateinit var presenter: MainPresenter

    private val navigator = AppNavigator(this, supportFragmentManager, R.id.fragmentsContainer)

    private val navigatorHolder: NavigatorHolder by instance()

    override fun module(): Kodein.Module = Kodein.Module(MainActivity::class.java.canonicalName) {
        bind<MainPresenter>() with singleton { MainPresenter(kodein) }
    }

    @ProvidePresenter
    fun presenterProvider() = providePresenter<MainPresenter>()

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)

        presenter.handleNavigation(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()

        navigatorHolder.setNavigator(navigator)
    }

    override fun onPause() {
        super.onPause()

        navigatorHolder.removeNavigator()
    }
}