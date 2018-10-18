package com.soramitsu.test.weathersoramitsu

import android.app.Application
import com.arellomobile.mvp.RegisterMoxyReflectorPackages
import com.soramitsu.test.repository.RepositoryModule
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import timber.log.Timber

@RegisterMoxyReflectorPackages("com.soramitsu.test.domain")
class App : Application(), KodeinAware {

    override val kodein = Kodein.lazy {
        import(appModule(applicationContext))
        import(RepositoryModule(this@App).repositoryKodein)
    }

    override fun onCreate() {
        super.onCreate()

        initTimber()
    }

    private fun initTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}