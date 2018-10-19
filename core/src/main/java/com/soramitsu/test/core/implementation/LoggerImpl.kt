package com.soramitsu.test.core.implementation

import com.soramitsu.test.core.BuildConfig
import com.soramitsu.test.domain.interfaces.Logger
import timber.log.Timber

class LoggerImpl : Logger {

    override fun logDebug(message: String, tag: String) {
        if (BuildConfig.DEBUG) {
            Timber.tag(tag).d(message)
        }
    }

    override fun logErrorIfDebug(error: Throwable) {
        if (BuildConfig.DEBUG) {
            Timber.e(error)
        }
    }
}