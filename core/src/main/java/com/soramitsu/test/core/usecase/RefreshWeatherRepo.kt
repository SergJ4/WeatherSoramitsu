package com.soramitsu.test.core.usecase

import com.soramitsu.test.core.base.SwipeRefresh
import com.soramitsu.test.domain.SchedulersProvider
import com.soramitsu.test.domain.interfaces.ProgressBus
import com.soramitsu.test.domain.interfaces.WeatherRepository
import io.reactivex.Completable

class RefreshWeatherRepo(
    private val swipeRefreshListener: SwipeRefresh,
    private val weatherRepository: WeatherRepository,
    private val progressBus: ProgressBus
) {

    operator fun invoke(): Completable =
        swipeRefreshListener()
            .observeOn(SchedulersProvider.ui())
            .doOnNext { progressBus.show() }
            .observeOn(SchedulersProvider.io())
            .flatMapCompletable {
                weatherRepository.refresh()
            }
            .observeOn(SchedulersProvider.ui())
            .doOnComplete { progressBus.hide() }
}