package com.soramitsu.test.domain.base

import android.support.design.widget.Snackbar
import android.support.v4.widget.SwipeRefreshLayout
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.soramitsu.test.domain.R
import org.jetbrains.anko.contentView
import org.jetbrains.anko.find
import org.jetbrains.anko.findOptional
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.direct
import org.kodein.di.generic.instance

abstract class BaseActivity<P : BasePresenter<*>> : MvpAppCompatActivity(), BaseView, KodeinAware {

    private val parentKodein by closestKodein()

    override val kodein = Kodein.lazy {
        extend(parentKodein)
        import(module())
    }

    lateinit var presenter: P

    @ProvidePresenter
    inline fun <reified P : BasePresenter<*>> providePresenter(): P = kodein.direct.instance()

    abstract fun module(): Kodein.Module

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