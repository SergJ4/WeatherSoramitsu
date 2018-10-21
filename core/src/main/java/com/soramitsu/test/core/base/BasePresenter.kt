package com.soramitsu.test.core.base

import android.content.Context
import com.arellomobile.mvp.MvpPresenter
import com.soramitsu.test.core.R
import com.soramitsu.test.domain.exceptions.NetworkConnectionError
import com.soramitsu.test.domain.exceptions.RefreshDataError
import com.soramitsu.test.domain.exceptions.ResourceNotFoundError
import com.soramitsu.test.domain.interfaces.*
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.generic.instance
import java.util.concurrent.TimeUnit

abstract class BasePresenter<V : BaseView>(override val kodein: Kodein) : MvpPresenter<V>(),
    KodeinAware {

    protected val router: Router by instance()
    protected val messageBus: MessageBus by instance()
    protected val progressBus: ProgressBus by instance()
    protected val logger: Logger by instance()
    protected val executor: Executor by instance()
    protected val apiErrors: ApiErrors by instance()
    private val context: Context by instance()

    init {
        executor
            .subscribeOnUi(progressBus.listen()) { shouldShow ->
                if (shouldShow) {
                    viewState.showProgress()
                } else {
                    viewState.hideProgress()
                }
            }

        executor
            .subscribeOnUi(messageBus.listen()) { message ->
                viewState.showMessage(message)
            }

        executor
            .executeAsync(
                apiErrors()
                    .debounce(600, TimeUnit.MILLISECONDS)
            ) {
                when (it) {
                    is NetworkConnectionError -> viewState.showMessage(context.getString(R.string.no_network_error))
                    is ResourceNotFoundError -> viewState.showMessage(context.getString(R.string.city_not_found))
                    is RefreshDataError -> viewState.showMessage(context.getString(R.string.refresh_data_error))
                }
                viewState.hideProgress()
            }
    }

    override fun onDestroy() {
        super.onDestroy()
        executor.onDestroy()
    }
}