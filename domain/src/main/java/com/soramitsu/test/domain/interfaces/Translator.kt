package com.soramitsu.test.domain.interfaces

import io.reactivex.Single

interface Translator {

    fun translate(textToTranslate: String, toLanguage: String): Single<String>
}