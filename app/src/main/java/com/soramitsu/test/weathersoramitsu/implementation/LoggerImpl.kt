package com.soramitsu.test.weathersoramitsu.implementation

import com.soramitsu.test.domain.interfaces.Logger
import com.soramitsu.test.weathersoramitsu.BuildConfig
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