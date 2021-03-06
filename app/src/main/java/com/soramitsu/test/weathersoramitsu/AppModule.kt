package com.soramitsu.test.weathersoramitsu

import android.content.Context
import com.soramitsu.test.core.implementation.LoggerImpl
import com.soramitsu.test.domain.interfaces.ApiErrors
import com.soramitsu.test.domain.interfaces.ImageLoader
import com.soramitsu.test.domain.interfaces.Logger
import com.soramitsu.test.repository.ImageLoaderImpl
import io.reactivex.disposables.CompositeDisposable
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton
import ru.terrakok.cicerone.Cicerone
import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.cicerone.Router

fun appModule(appContext: Context) = Kodein.Module(name = "AppModule") {
    bind<Context>() with singleton { appContext }

    bind<Cicerone<Router>>() with singleton { Cicerone.create() }
    bind<NavigatorHolder>() with singleton { instance<Cicerone<Router>>().navigatorHolder }
    bind<Router>() with singleton { instance<Cicerone<Router>>().router }

    bind<com.soramitsu.test.domain.interfaces.Router>() with singleton {
        RouterImpl(
            instance()
        )
    }
    bind<ImageLoader>() with singleton { ImageLoaderImpl() }
    bind<Logger>() with singleton { LoggerImpl() }

    bind<CompositeDisposable>() with provider { CompositeDisposable() }
    bind<ApiErrors>() with singleton { ApiErrors() }
}