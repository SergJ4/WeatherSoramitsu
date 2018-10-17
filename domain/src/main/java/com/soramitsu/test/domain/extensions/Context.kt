package com.soramitsu.test.domain.extensions

import android.content.Context
import android.content.Context.CONNECTIVITY_SERVICE
import android.graphics.drawable.Drawable
import android.net.ConnectivityManager
import android.support.annotation.ColorRes
import android.support.annotation.DrawableRes
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.View

fun Context.hasNetwork(): Boolean {
    val connectivityManager = getSystemService(CONNECTIVITY_SERVICE) as? ConnectivityManager
    val activeNetworkInfo = connectivityManager?.activeNetworkInfo
    return activeNetworkInfo?.isConnected ?: false
}

fun Context.color(@ColorRes colorRes: Int): Int = ContextCompat.getColor(this, colorRes)

fun Fragment.color(@ColorRes colorRes: Int): Int = ContextCompat.getColor(context!!, colorRes)

fun View.color(@ColorRes colorRes: Int): Int = ContextCompat.getColor(context!!, colorRes)

fun Context.drawable(@DrawableRes drawableRes: Int): Drawable =
    ContextCompat.getDrawable(this, drawableRes)!!

fun Fragment.drawable(@DrawableRes drawableRes: Int): Drawable =
    ContextCompat.getDrawable(context!!, drawableRes)!!

fun View.drawable(@DrawableRes drawableRes: Int): Drawable =
    ContextCompat.getDrawable(context!!, drawableRes)!!