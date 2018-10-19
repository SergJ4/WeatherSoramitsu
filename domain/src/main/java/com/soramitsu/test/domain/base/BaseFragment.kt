package com.soramitsu.test.domain.base

import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.design.widget.Snackbar
import android.support.v4.widget.SwipeRefreshLayout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arellomobile.mvp.MvpAppCompatFragment
import com.soramitsu.test.domain.R
import org.jetbrains.anko.findOptional
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.support.closestKodein
import org.kodein.di.direct
import org.kodein.di.generic.instance

abstract class BaseFragment : MvpAppCompatFragment(), BaseView, KodeinAware {

    private val parentKodein by closestKodein()

    override val kodein = Kodein.lazy {
        extend(parentKodein)
        import(module())
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