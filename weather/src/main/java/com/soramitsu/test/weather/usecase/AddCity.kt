package com.soramitsu.test.weather.usecase

import com.soramitsu.test.domain.SchedulersProvider
import com.soramitsu.test.domain.interfaces.ProgressBus
import com.soramitsu.test.domain.interfaces.WeatherRepository
import io.reactivex.Observable

class AddCity(
    private val googlePlaces: GooglePlaces,
    private val weatherRepository: WeatherRepository,
    private val progressBus: ProgressBus
) {

    operator fun invoke(): Observable<Any> =
        googlePlaces
            .selectedCity()
            .observeOn(SchedulersProvider.ui())
            .doOnNext { progressBus.show() }
            .observeOn(SchedulersProvider.io())
            .switchMap {
                weatherRepository.addCity(it.name).toObservable<Any>()
            }
            .observeOn(SchedulersProvider.ui())
            .doOnNext { progressBus.hide() }
}