package com.soramitsu.test.domain.extensions

import android.support.v4.widget.SwipeRefreshLayout
import com.soramitsu.test.domain.R
import org.jetbrains.anko.dip

fun SwipeRefreshLayout.applyDefaultStyle(progressViewOffset: Int = 60) {
    setSize(SwipeRefreshLayout.DEFAULT)
    setColorSchemeResources(R.color.primary, R.color.primaryDark)
    setDistanceToTriggerSync(200)
    setProgressViewOffset(true, 0, dip(progressViewOffset))
}