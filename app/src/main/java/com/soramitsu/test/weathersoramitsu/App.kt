package com.soramitsu.test.weathersoramitsu

import android.app.Application
import com.arellomobile.mvp.RegisterMoxyReflectorPackages
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware

@RegisterMoxyReflectorPackages("com.soramitsu.test.domain")
class App : Application(), KodeinAware {
    override val kodein = Kodein.lazy {
        import(appModule(applicationContext))
    }
}