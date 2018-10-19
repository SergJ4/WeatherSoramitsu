package com.soramitsu.test.core.base

import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.design.widget.Snackbar
import android.support.v4.widget.SwipeRefreshLayout
import com.arellomobile.mvp.MvpAppCompatActivity
import com.soramitsu.test.core.implementation.ExecutorImpl
import com.soramitsu.test.core.implementation.MessageBusImpl
import com.soramitsu.test.core.implementation.ProgressBusImpl
import com.soramitsu.test.domain.R
import com.soramitsu.test.domain.interfaces.Executor
import com.soramitsu.test.domain.interfaces.MessageBus
import com.soramitsu.test.domain.interfaces.ProgressBus
import org.jetbrains.anko.contentView
import org.jetbrains.anko.find
import org.jetbrains.anko.findOptional
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
        findOptional<SwipeRefreshLayout>(R.id.swipeRefresh)
            ?.isRefreshing = true
    }

    override fun hideProgress() {
        findOptional<SwipeRefreshLayout>(R.id.swipeRefresh)
            ?.isRefreshing = false
    }

    override fun showMessage(message: String) {
        val snackbar = Snackbar.make(
            contentView ?: find(android.R.id.content),
            message,
            Snackbar.LENGTH_LONG
        )

        snackbar.show()
    }
}