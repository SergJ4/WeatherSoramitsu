package com.soramitsu.test.core

import android.content.Context
import android.support.design.widget.CoordinatorLayout
import android.support.design.widget.FloatingActionButton
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.View

/**
 * Hides and shows FAB depending on RecyclerView`s scroll direction
 */
class FABScrollBehavior(context: Context, attributeSet: AttributeSet) :
    FloatingActionButton.Behavior(context, attributeSet) {

    private var recyclerView: RecyclerView? = null

    override fun onDependentViewChanged(
        parent: CoordinatorLayout,
        child: FloatingActionButton,
        dependency: View
    ): Boolean {
        fun handleFABVisibility(dyConsumed: Int) {
            if (dyConsumed > 0 && child.visibility == View.VISIBLE) {
                child.hide(object : FloatingActionButton.OnVisibilityChangedListener() {
                    override fun onHidden(fab: FloatingActionButton) {
                        super.onHidden(fab)
                        fab.hide()
                    }
                })
            } else if (dyConsumed < 0 && child.visibility != View.VISIBLE) {
                child.show()
            }
        }

        if (recyclerView == null && dependency is RecyclerView) {
            recyclerView = dependency
            dependency.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    handleFABVisibility(dy)
                }
            })
        }
        return super.onDependentViewChanged(parent, child, dependency)
    }
}