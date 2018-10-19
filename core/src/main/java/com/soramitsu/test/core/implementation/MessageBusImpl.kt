package com.soramitsu.test.core.implementation

import android.content.Context
import com.likemeal.domain.ObservableField
import com.soramitsu.test.domain.interfaces.MessageBus
import io.reactivex.Observable

class MessageBusImpl(private val context: Context) : MessageBus, ObservableField<String>() {

    override fun showMessage(message: String, resId: Int) {
        if (resId > 0) {
            this(context.getString(resId))
        } else if (message.isNotBlank()) {
            this(message)
        }
    }

    override fun listen(): Observable<String> = this()
}