package com.soramitsu.test.weather.usecase

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.support.v4.app.Fragment
import com.google.android.gms.location.places.AutocompleteFilter
import com.google.android.gms.location.places.ui.PlaceAutocomplete
import com.soramitsu.test.domain.SchedulersProvider
import com.soramitsu.test.domain.interfaces.Logger
import com.soramitsu.test.domain.interfaces.MessageBus
import com.soramitsu.test.domain.interfaces.Translator
import com.soramitsu.test.domain.models.SearchCity
import com.soramitsu.test.weather.R
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject
import java.util.*


class GooglePlaces(
    private val logger: Logger,
    private val messageBus: MessageBus,
    private val translator: Translator
) {

    private val requestCode = 999
    private val citySubject: PublishSubject<SearchCity> = PublishSubject.create()
    private val compositeDisposable = CompositeDisposable()

    fun showCitiesList(fragment: Fragment) {
        try {
            val activity = fragment.activity
            val typeFilter = AutocompleteFilter
                .Builder()
                .setTypeFilter(AutocompleteFilter.TYPE_FILTER_CITIES)
                .build()

            val intent = PlaceAutocomplete
                .IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                .setFilter(typeFilter)
                .build(activity)

            fragment.startActivityForResult(intent, requestCode)
        } catch (e: Exception) {
            logger.logErrorIfDebug(e)
            messageBus.showMessage(resId = R.string.unknown_error)
        }
    }

    fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        resultIntent: Intent?,
        fragment: Fragment
    ): Boolean {
        return if (requestCode == this.requestCode &&
            resultCode == RESULT_OK &&
            resultIntent != null
        ) {
            val place = PlaceAutocomplete.getPlace(fragment.activity, resultIntent)

            if (Locale.getDefault().language != "en") {
                compositeDisposable.add(
                    translator
                        .translate(place.name.toString(), "en")
                        .subscribeOn(SchedulersProvider.io())
                        .observeOn(SchedulersProvider.ui())
                        .subscribe { result ->
                            val city = SearchCity(
                                id = place.id,
                                name = result
                            )
                            citySubject.onNext(city)
                        }
                )
            } else {
                val city = SearchCity(
                    id = place.id,
                    name = place.name.toString()
                )
                citySubject.onNext(city)
            }
            true
        } else {
            false
        }
    }

    fun selectedCity(): Observable<SearchCity> = citySubject.hide()
}