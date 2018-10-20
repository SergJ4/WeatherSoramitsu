package com.soramitsu.test.core.base

import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.design.widget.Snackbar
import android.support.v4.widget.SwipeRefreshLayout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arellomobile.mvp.MvpAppCompatFragment
import com.soramitsu.test.core.implementation.ExecutorImpl
import com.soramitsu.test.core.implementation.MessageBusImpl
import com.soramitsu.test.core.implementation.ProgressBusImpl
import com.soramitsu.test.domain.R
import com.soramitsu.test.domain.extensions.applyDefaultStyle
import com.soramitsu.test.domain.interfaces.Executor
import com.soramitsu.test.domain.interfaces.MessageBus
import com.soramitsu.test.domain.interfaces.ProgressBus
import org.jetbrains.anko.findOptional
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.support.closestKodein
import org.kodein.di.direct
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton

abstract class BaseFragment : MvpAppCompatFragment(), BaseView, KodeinAware {

    private val parentKodein by closestKodein()

    override val kodein = Kodein.lazy {
        extend(parentKodein)
        import(module())
        bind<MessageBus>(overrides = true) with singleton { MessageBusImpl(instance()) }
        bind<ProgressBus>(overrides = true) with singleton { ProgressBusImpl() }
        bind<Executor>(overrides = true) with singleton {
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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(layoutRes, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findOptional<SwipeRefreshLayout>(R.id.swipeRefresh)?.applyDefaultStyle()
    }

    override fun showProgress() {
        view?.findOptional<SwipeRefreshLayout>(R.id.swipeRefresh)
            ?.isRefreshing = true
    }

    override fun hideProgress() {
        view?.findOptional<SwipeRefreshLayout>(R.id.swipeRefresh)
            ?.isRefreshing = false
    }

    override fun showMessage(message: String) {
        if (view != null) {
            val snackbar = Snackbar.make(
                view!!,
                message,
                Snackbar.LENGTH_LONG
            )

            snackbar.show()
        }
    }
}