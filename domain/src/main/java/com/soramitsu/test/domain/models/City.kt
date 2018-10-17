package com.soramitsu.test.domain.models

data class City(
    val id: String,
    val title: String,
    val weatherPrediction: List<Weather>
)