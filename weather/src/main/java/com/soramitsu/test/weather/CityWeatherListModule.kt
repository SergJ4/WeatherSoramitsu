package com.soramitsu.test.weather

import org.kodein.di.Kodein

fun cityWeatherListModule() = Kodein.Module(CityWeatherListFragment::class.java.canonicalName) {

}