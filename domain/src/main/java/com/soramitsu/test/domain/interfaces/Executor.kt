package com.soramitsu.test.domain.interfaces

import io.reactivex.Observable
import io.reactivex.Single

interface Executor {

    val messageBus: MessageBus
    val progressBarBus: ProgressBus
    val logger: Logger

    fun <T> executeAsync(
        observable: Observable<T>,
        onError: (Throwable) -> Unit = { throwable ->
            progressBarBus.hide()
            logger.logErrorIfDebug(throwable)
        },
        onComplete: () -> Unit = {},
        onNext: (T) -> Unit = {}
    )

    fun <T> executeAsync(
        observable: Single<T>,
        onError: (Throwable) -> Unit = { throwable ->
            progressBarBus.hide()
            logger.logErrorIfDebug(throwable)
        },
        onSuccess: (T) -> Unit = {}
    )

    fun <T> subscribeOnUi(
        observable: Observable<T>,
        onError: (Throwable) -> Unit = { throwable ->
            progressBarBus.hide()
            logger.logErrorIfDebug(throwable)
        },
        onComplete: () -> Unit = {},
        onNext: (T) -> Unit = {}
    )

    fun onDestroy()
}