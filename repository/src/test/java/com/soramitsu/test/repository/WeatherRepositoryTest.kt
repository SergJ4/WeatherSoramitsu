package com.soramitsu.test.repository

import com.soramitsu.test.domain.interfaces.ApiErrors
import com.soramitsu.test.domain.interfaces.Logger
import com.soramitsu.test.repository.datasource.api.ApiDataSource
import com.soramitsu.test.repository.datasource.db.DbDataSource
import com.soramitsu.test.repository.model.db.City
import com.soramitsu.test.repository.model.db.CityWithWeather
import com.soramitsu.test.repository.model.db.WeatherForecast
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subscribers.TestSubscriber
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class WeatherRepositoryTest {

    @get:Rule
    val rxRule = RxImmediateSchedulerRule()

    @MockK
    private lateinit var apiDataSource: ApiDataSource
    @MockK
    private lateinit var dbDataSource: DbDataSource
    @MockK
    private lateinit var apiError: ApiErrors
    @MockK
    private lateinit var logger: Logger

    private lateinit var weatherRepository: WeatherRepository

    private val mockedForecastWeather = listOf(
        WeatherForecast(
            1,
            WeatherForecast.ForecastDay.CURRENT_WEATHER,
            "test1",
            1f,
            10f,
            750f,
            2f,
            "icon1",
            1
        ),
        WeatherForecast(
            2,
            WeatherForecast.ForecastDay.TOMORROW,
            "test2",
            1f,
            10f,
            750f,
            2f,
            "icon2",
            1
        ),
        WeatherForecast(
            1,
            WeatherForecast.ForecastDay.AFTER_TOMORROW,
            "test3",
            1f,
            10f,
            750f,
            2f,
            "icon3",
            1
        )
    )

    private val mockedCity = City(1, "testCity")

    private val mockedCitiesWithWeather = listOf(
        CityWithWeather().apply {
            city = mockedCity
            weather = mockedForecastWeather
        }
    )

    @Before
    fun setup() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `cities current weather from db is converted to domain models`() {
        //prevent refresh chain to be called on weatherRepository initialization
        every { dbDataSource.getCitiesInDb() } returns Single.just(listOf())

        weatherRepository = WeatherRepository(
            apiDataSource,
            dbDataSource,
            CompositeDisposable(),
            apiError,
            logger
        )

        val testSubscriber = TestSubscriber<List<com.soramitsu.test.domain.models.City>>()

        every { dbDataSource.fetchCurrentWeather() } returns Flowable.just(mockedCitiesWithWeather)

        weatherRepository
            .observeCitiesCurrentWeather()
            .subscribe(testSubscriber)

        testSubscriber
            .assertNoErrors()
            .assertValue(
                mockedCitiesWithWeather.map { it.toDomainModel() }
            )
    }
}