package com.soramitsu.test.domain.extensions

import java.text.SimpleDateFormat
import java.util.*

fun Calendar.toFormattedString(): String {
    val formatter = SimpleDateFormat.getDateInstance()
    return formatter.format(time)
}