package com.soramitsu.test.repository

import com.soramitsu.test.domain.exceptions.*
import com.soramitsu.test.domain.interfaces.ApiErrors
import com.soramitsu.test.domain.interfaces.Logger
import com.soramitsu.test.repository.datasource.api.ApiDataSource
import com.soramitsu.test.repository.datasource.db.DbDataSource
import com.soramitsu.test.repository.model.api.*
import com.soramitsu.test.repository.model.db.City
import com.soramitsu.test.repository.model.db.CityWithWeather
import com.soramitsu.test.repository.model.db.WeatherForecast
import io.mockk.*
import io.mockk.impl.annotations.MockK
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.TestObserver
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
    private lateinit var logger: Logger

    private lateinit var weatherRepository: WeatherRepository
    private val apiError: ApiErrors = ApiErrors()

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

    private val mockedApiCurrentWeather = ApiCurrentWeatherResponse(
        ApiWeatherMainInfo(0f, 0f, 0f),
        listOf(),
        ApiWind(0f),
        -1,
        "testCity"
    )

    private val mockedApiForecastWeather = ApiForecastWeatherResponse(
        ApiCity(-1, "testCity"),
        listOf()
    )

    private val mockedApiGroupedResponse = ApiGroupedWeatherResponse(
        listOf(mockedApiCurrentWeather)
    )

    @Before
    fun setup() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `when initialized then current cities refreshed`() {
        every { dbDataSource.getCitiesInDb() } returns Single.just(listOf(mockedCity))
        every { apiDataSource.fetchCurrentWeatherForAll(any()) } returns
                Single.just(mockedApiGroupedResponse)
        every { apiDataSource.fetchForecastWeather(cityId = any()) } returns Single.just(
            mockedApiForecastWeather
        )
        every { dbDataSource.insertOrUpdateCurrentWeather(any()) } just Runs
        every { dbDataSource.insertOrUpdateForecast(any()) } just Runs

        weatherRepository = WeatherRepository(
            apiDataSource,
            dbDataSource,
            CompositeDisposable(),
            apiError,
            logger
        )

        verifyOrder {
            dbDataSource.getCitiesInDb()
            apiDataSource.fetchCurrentWeatherForAll(listOf(mockedCity))
            apiDataSource.fetchForecastWeather(cityId = mockedCity.id)
            dbDataSource.insertOrUpdateCurrentWeather(mockedApiGroupedResponse.weatherList)
            dbDataSource.insertOrUpdateForecast(listOf(mockedApiForecastWeather))
        }
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

        every { dbDataSource.fetchCurrentWeather() } returns Flowable.just(mockedCitiesWithWeather)

        weatherRepository
            .observeCitiesCurrentWeather()
            .test()
            .assertNoErrors()
            .assertValue(
                mockedCitiesWithWeather.map { it.toDomainModel() }
            )

        verify(exactly = 1) { dbDataSource.fetchCurrentWeather() }
    }

    @Test
    fun `exact city weather from db is converted to domain model`() {
        //prevent refresh chain to be called on weatherRepository initialization
        every { dbDataSource.getCitiesInDb() } returns Single.just(listOf())

        weatherRepository = WeatherRepository(
            apiDataSource,
            dbDataSource,
            CompositeDisposable(),
            apiError,
            logger
        )

        every { dbDataSource.fetchForecastWeatherForCityId(any()) } returns
                Flowable.just(mockedCitiesWithWeather[0])

        weatherRepository
            .observeExactCityWeather(mockedCitiesWithWeather[0].city!!.id)
            .test()
            .assertNoErrors()
            .assertValue(
                mockedCitiesWithWeather[0].toDomainModel()
            )

        verify(exactly = 1) { dbDataSource.fetchForecastWeatherForCityId(mockedCitiesWithWeather[0].city!!.id) }
    }

    @Test
    fun `when addCity and no api errors then all calls correct`() {
        //prevent refresh chain to be called on weatherRepository initialization
        every { dbDataSource.getCitiesInDb() } returns Single.just(listOf())

        weatherRepository = WeatherRepository(
            apiDataSource,
            dbDataSource,
            CompositeDisposable(),
            apiError,
            logger
        )

        every { apiDataSource.fetchCurrentWeather(any()) } returns Single.just(
            mockedApiCurrentWeather
        )
        every { apiDataSource.fetchForecastWeather(cityId = any()) } returns Single.just(
            mockedApiForecastWeather
        )
        every { dbDataSource.addCity(any()) } just Runs
        every { dbDataSource.insertOrUpdateCurrentWeather(any()) } just Runs
        every { dbDataSource.insertOrUpdateForecast(any()) } just Runs

        weatherRepository
            .addCity("testCity")
            .test()
            .assertNoErrors()
            .assertComplete()

        verifyOrder {
            apiDataSource.fetchCurrentWeather("testCity")
            dbDataSource.addCity(
                City(
                    mockedApiCurrentWeather.cityId,
                    mockedApiCurrentWeather.cityName
                )
            )
            dbDataSource.insertOrUpdateCurrentWeather(listOf(mockedApiCurrentWeather))
            apiDataSource.fetchForecastWeather(cityId = mockedApiCurrentWeather.cityId)
            dbDataSource.insertOrUpdateForecast(listOf(mockedApiForecastWeather))
        }
    }

    @Test
    fun `when city not found then apiError emits error`() {
        //prevent refresh chain to be called on weatherRepository initialization
        every { dbDataSource.getCitiesInDb() } returns Single.just(listOf())

        weatherRepository = WeatherRepository(
            apiDataSource,
            dbDataSource,
            CompositeDisposable(),
            apiError,
            logger
        )

        val testObserver = TestObserver<Error>()
        apiError()
            .subscribe(testObserver)

        every { apiDataSource.fetchCurrentWeather(any()) } returns
                Single.error(NetworkErrorException(NOT_FOUND))

        every { logger.logErrorIfDebug(any()) } just Runs

        weatherRepository
            .addCity("testCity")
            .test()
            .assertNoErrors()
            .assertComplete()

        verify(exactly = 0) {
            dbDataSource.addCity(any())
            dbDataSource.insertOrUpdateCurrentWeather(any())
            apiDataSource.fetchForecastWeather(cityId = any())
            dbDataSource.insertOrUpdateForecast(any())
        }
        testObserver.assertValue(ResourceNotFoundError)
    }

    @Test
    fun `when network exception then apiError emits error`() {
        //prevent refresh chain to be called on weatherRepository initialization
        every { dbDataSource.getCitiesInDb() } returns Single.just(listOf())

        weatherRepository = WeatherRepository(
            apiDataSource,
            dbDataSource,
            CompositeDisposable(),
            apiError,
            logger
        )

        val testObserver = TestObserver<Error>()
        apiError()
            .subscribe(testObserver)

        every { apiDataSource.fetchCurrentWeather(any()) } returns
                Single.error(NetworkErrorException())

        every { logger.logErrorIfDebug(any()) } just Runs

        weatherRepository
            .addCity("testCity")
            .test()
            .assertNoErrors()
            .assertComplete()

        testObserver.assertValue(NetworkConnectionError)
    }

    @Test
    fun `when other exception then apiError emits error`() {
        //prevent refresh chain to be called on weatherRepository initialization
        every { dbDataSource.getCitiesInDb() } returns Single.just(listOf())

        weatherRepository = WeatherRepository(
            apiDataSource,
            dbDataSource,
            CompositeDisposable(),
            apiError,
            logger
        )

        val testObserver = TestObserver<Error>()
        apiError()
            .subscribe(testObserver)

        every { apiDataSource.fetchCurrentWeather(any()) } returns
                Single.error(Throwable())

        every { logger.logErrorIfDebug(any()) } just Runs

        weatherRepository
            .addCity("testCity")
            .test()
            .assertNoErrors()
            .assertComplete()

        testObserver.assertValue(RefreshDataError)
    }
}