package com.soramitsu.test.core.base

import com.arellomobile.mvp.MvpPresenter
import com.soramitsu.test.domain.interfaces.*
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.generic.instance

abstract class BasePresenter<V : BaseView>(override val kodein: Kodein) : MvpPresenter<V>(),
    KodeinAware {

    protected val router: Router by instance()
    protected val messageBus: MessageBus by instance()
    protected val progressBus: ProgressBus by instance()
    protected val logger: Logger by instance()
    protected val executor: Executor by instance()

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
    }

    override fun onDestroy() {
        super.onDestroy()
        executor.onDestroy()
    }
}