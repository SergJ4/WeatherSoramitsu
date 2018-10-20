package com.soramitsu.test.core.base

import android.os.Bundle
import android.support.annotation.LayoutRes
import com.arellomobile.mvp.MvpAppCompatActivity
import com.soramitsu.test.core.implementation.ExecutorImpl
import com.soramitsu.test.core.implementation.MessageBusImpl
import com.soramitsu.test.core.implementation.ProgressBusImpl
import com.soramitsu.test.domain.interfaces.Executor
import com.soramitsu.test.domain.interfaces.MessageBus
import com.soramitsu.test.domain.interfaces.ProgressBus
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.direct
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton

abstract class BaseActivity : MvpAppCompatActivity(), BaseView, KodeinAware {

    private val parentKodein by closestKodein()

    override val kodein = Kodein.lazy {
        extend(parentKodein)
        import(module())
        bind<MessageBus>() with singleton { MessageBusImpl(instance()) }
        bind<ProgressBus>() with singleton { ProgressBusImpl() }
        bind<Executor>() with singleton {
            ExecutorImpl(
                instance(),
                instance(),
                instance(),
                instance()
            )
        }
    }

    @get:LayoutRes
    protected abstract val layoutRes: Int

    inline fun <reified P : BasePresenter<*>> providePresenter(): P = kodein.direct.instance()

    abstract fun module(): Kodein.Module

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutRes)
    }

    override fun showProgress() {
        // progress bar visibility controlled by fragments
    }

    override fun hideProgress() {
        // progress bar visibility controlled by fragments
    }

    override fun showMessage(message: String) {
        // all messages shown by fragments
    }
}