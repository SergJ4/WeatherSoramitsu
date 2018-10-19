package com.soramitsu.weathersoramitsu

import android.os.Bundle
import com.soramitsu.test.domain.interfaces.*
import com.soramitsu.test.weathersoramitsu.MainPresenter
import io.mockk.*
import io.mockk.impl.annotations.MockK
import io.reactivex.Observable
import org.junit.Before
import org.junit.Test
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance

class MainPresenterTest {

    @MockK
    private lateinit var executor: Executor
    @MockK
    private lateinit var progressBus: ProgressBus
    @MockK
    private lateinit var messageBus: MessageBus
    @MockK
    private lateinit var router: Router
    @MockK
    private lateinit var savedState: Bundle

    private lateinit var mainPresenter: MainPresenter

    private val kodein = Kodein.lazy {
        bind<Executor>() with instance(executor)
        bind<ProgressBus>() with instance(progressBus)
        bind<MessageBus>() with instance(messageBus)
        bind<Router>() with instance(router)
    }

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        every { executor.subscribeOnUi(any<Observable<*>>(), any(), any(), any()) } just Runs
        every { router.rootScreen(any()) } just Runs
        every { progressBus.listen() } returns Observable.never()
        every { messageBus.listen() } returns Observable.never()

        mainPresenter = MainPresenter(kodein)
    }

    @Test
    fun `if activity recreated then router should not be called`() {

        mainPresenter.handleNavigation(savedState)

        verify(exactly = 0) { router.rootScreen(any()) }
    }

    @Test
    fun `if activity not recreated then router should be called`() {

        mainPresenter.handleNavigation(null)

        verify(exactly = 1) { router.rootScreen(WEATHER_LIST_SCREEN) }
    }
}