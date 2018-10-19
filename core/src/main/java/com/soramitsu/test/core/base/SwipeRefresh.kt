package com.soramitsu.test.core.base

import android.support.v4.widget.SwipeRefreshLayout
import com.likemeal.domain.ObservableField
import com.soramitsu.test.domain.models.Trigger

class SwipeRefresh : ObservableField<Trigger>(), SwipeRefreshLayout.OnRefreshListener {

    override fun onRefresh() {
        this(Trigger)
    }
}