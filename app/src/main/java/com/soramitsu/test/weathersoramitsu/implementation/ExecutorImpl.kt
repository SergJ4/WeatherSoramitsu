package com.soramitsu.test.weathersoramitsu.implementation

import com.soramitsu.test.domain.base.SchedulersProvider
import com.soramitsu.test.domain.interfaces.Executor
import com.soramitsu.test.domain.interfaces.Logger
import com.soramitsu.test.domain.interfaces.MessageBus
import com.soramitsu.test.domain.interfaces.ProgressBus
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable

class ExecutorImpl(
    override val messageBus: MessageBus,
    override val progressBarBus: ProgressBus,
    override val logger: Logger,
    private val compositeDisposable: CompositeDisposable
) : Executor {

    override fun <T> executeAsync(
        observable: Observable<T>,
        onError: (Throwable) -> Unit,
        onComplete: () -> Unit,
        onNext: (T) -> Unit
    ) {
        compositeDisposable.add(
            observable
                .subscribeOn(SchedulersProvider.io())
                .observeOn(SchedulersProvider.ui())
                .subscribe(onNext, onError, onComplete)
        )
    }

    override fun <T> executeAsync(
        observable: Single<T>,
        onError: (Throwable) -> Unit,
        onSuccess: (T) -> Unit
    ) {
        compositeDisposable.add(
            observable.subscribeOn(SchedulersProvider.io())
                .observeOn(SchedulersProvider.ui())
                .subscribe(onSuccess, onError)
        )
    }

    override fun <T> subscribeOnUi(
        observable: Observable<T>,
        onError: (Throwable) -> Unit,
        onComplete: () -> Unit,
        onNext: (T) -> Unit
    ) {
        compositeDisposable.add(
            observable.subscribeOn(SchedulersProvider.ui())
                .observeOn(SchedulersProvider.ui())
                .subscribe(onNext, onError)
        )
    }

    override fun onDestroy() = compositeDisposable.dispose()
}