package com.soramitsu.test.domain.base

import android.support.v4.widget.SwipeRefreshLayout
import com.likemeal.domain.ObservableField

class SwipeRefresh : ObservableField<Trigger>(), SwipeRefreshLayout.OnRefreshListener {

    override fun onRefresh() {
        this(Trigger)
    }
}