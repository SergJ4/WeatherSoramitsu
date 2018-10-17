package com.soramitsu.test.weathersoramitsu

import android.app.Application
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware

class App : Application(), KodeinAware {
    override val kodein = Kodein.lazy {
        import(appModule(applicationContext))
    }
}