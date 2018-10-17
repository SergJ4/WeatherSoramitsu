package com.soramitsu.test.weathersoramitsu.implementation

import com.likemeal.domain.CachedField
import com.soramitsu.test.domain.interfaces.ProgressBus
import io.reactivex.Observable

class ProgressBusImpl : ProgressBus, CachedField<Boolean>(false) {

    override fun show() = this(true)

    override fun hide() = this(false)

    override fun listen(): Observable<Boolean> = this()
}