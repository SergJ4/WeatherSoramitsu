package com.likemeal.domain

import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject

abstract class CachedField<T>() {

    constructor(startWith: T) : this() {
        this.invoke(startWith)
    }

    private val valueSubject: BehaviorSubject<T> = BehaviorSubject.create()

    operator fun invoke(newValue: T) = valueSubject.onNext(newValue)

    operator fun invoke(): Observable<T> = valueSubject.hide()
}