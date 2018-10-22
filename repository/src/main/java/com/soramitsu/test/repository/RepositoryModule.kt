package com.soramitsu.test.repository

import android.arch.persistence.db.SupportSQLiteDatabase
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.soramitsu.test.domain.interfaces.Logger
import com.soramitsu.test.domain.interfaces.Translator
import com.soramitsu.test.repository.datasource.api.*
import com.soramitsu.test.repository.datasource.db.DB_NAME
import com.soramitsu.test.repository.datasource.db.DbDataSource
import com.soramitsu.test.repository.datasource.db.WeatherDao
import com.soramitsu.test.repository.datasource.db.WeatherDb
import com.soramitsu.test.repository.model.db.CITY_ID_COLUMN
import com.soramitsu.test.repository.model.db.CITY_NAME_COLUMN
import com.soramitsu.test.repository.model.db.CITY_TABLE
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

private const val WEATHER_BASE_URL = "https://api.openweathermap.org/data/2.5/"
const val WEATHER_ICON_URL = "http://openweathermap.org/img/w/"
private const val HTTP_LOG_TAG = "soramitsu_http"
private const val NETWORK_TIMEOUT = 6 //seconds
private const val WEATHER_RETROFIT = "weather"
private const val TRANSLATOR_RETROFIT = "translator"

private const val TRANSLATION_BASE_URL = "https://translate.yandex.net/api/v1.5/tr.json/"

class RepositoryModule(private val appContext: Context) {

    private val weatherDb: WeatherDb by lazy {
        Room
            .databaseBuilder(appContext, WeatherDb::class.java, DB_NAME)
            .addCallback(object : RoomDatabase.Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                    val moscow = ContentValues()
                    moscow.put(CITY_ID_COLUMN, 524901)
                    moscow.put(CITY_NAME_COLUMN, "Moscow")

                    val petersburg = ContentValues()
                    petersburg.put(CITY_ID_COLUMN, 498817)
                    petersburg.put(CITY_NAME_COLUMN, "Saint Petersburg")

                    db.insert(CITY_TABLE, SQLiteDatabase.CONFLICT_REPLACE, moscow)
                    db.insert(CITY_TABLE, SQLiteDatabase.CONFLICT_REPLACE, petersburg)
                }
            })
            .build()
    }

    val repositoryKodein = Kodein.Module("Repository module") {

        bind<Retrofit>(tag = WEATHER_RETROFIT) with singleton {
            createRetrofit(
                instance(),
                instance(),
                WEATHER_RETROFIT
            )
        }
        bind<Retrofit>(tag = TRANSLATOR_RETROFIT) with singleton {
            createRetrofit(
                instance(),
                instance(),
                TRANSLATOR_RETROFIT
            )
        }
        bind<WeatherApi>() with singleton {
            instance<Retrofit>(tag = WEATHER_RETROFIT).create(
                WeatherApi::class.java
            )
        }
        bind<TranslatorApi>() with singleton {
            instance<Retrofit>(tag = TRANSLATOR_RETROFIT).create(
                TranslatorApi::class.java
            )
        }
        bind<ApiDataSource>() with singleton { ApiDataSource(instance(), instance()) }

        bind<WeatherDb>() with instance(weatherDb)
        bind<WeatherDao>() with singleton { instance<WeatherDb>().weatherDao() }
        bind<DbDataSource>() with singleton { DbDataSource(instance()) }

        bind<WeatherRepository>() with singleton {
            WeatherRepository(
                instance(),
                instance(),
                instance(),
                instance(),
                instance()
            )
        }
        bind<Translator>() with singleton { TranslatorImpl(instance()) }
    }

    private fun createRetrofit(
        appContext: Context,
        logger: Logger,
        type: String
    ): Retrofit {
        val okHttpBuilder = OkHttpClient.Builder()

        val connectivityInterceptor = ConnectivityInterceptor(appContext)

        okHttpBuilder.addInterceptor(connectivityInterceptor)
        if (BuildConfig.DEBUG) {
            val loggingInterceptor = HttpLoggingInterceptor { message ->
                logger.logDebug(message, HTTP_LOG_TAG)
            }
                .setLevel(HttpLoggingInterceptor.Level.BODY)
            okHttpBuilder.addInterceptor(loggingInterceptor)
        }

        okHttpBuilder.connectTimeout(NETWORK_TIMEOUT.toLong(), TimeUnit.SECONDS)
        okHttpBuilder.readTimeout(NETWORK_TIMEOUT.toLong(), TimeUnit.SECONDS)
        okHttpBuilder.writeTimeout(NETWORK_TIMEOUT.toLong(), TimeUnit.SECONDS)

        val baseUrl = if (type == WEATHER_RETROFIT) {
            WEATHER_BASE_URL
        } else {
            TRANSLATION_BASE_URL
        }

        return Retrofit
            .Builder()
            .baseUrl(baseUrl)
            .client(okHttpBuilder.build())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}
