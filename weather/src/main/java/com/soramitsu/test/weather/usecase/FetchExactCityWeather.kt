package com.soramitsu.test.weather.usecase

import com.soramitsu.test.domain.interfaces.WeatherRepository
import com.soramitsu.test.domain.models.City
import io.reactivex.Flowable

class FetchExactCityWeather(private val weatherRepository: WeatherRepository) {

    operator fun invoke(cityId: Long): Flowable<City> =
        weatherRepository.observeExactCityWeather(cityId)
}