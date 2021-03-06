package com.soramitsu.test.weather.usecase

import com.soramitsu.test.domain.interfaces.WeatherRepository
import com.soramitsu.test.domain.models.City
import io.reactivex.Flowable

class FetchCitiesWithWeather(private val repository: WeatherRepository) {

    operator fun invoke(): Flowable<List<City>> = repository
        .observeCitiesCurrentWeather()
        .map { citites ->
            citites.filter {
                it.weatherPrediction.isNotEmpty() &&
                        !it.weatherPrediction[0].description.isNullOrBlank()
            }
        }
}