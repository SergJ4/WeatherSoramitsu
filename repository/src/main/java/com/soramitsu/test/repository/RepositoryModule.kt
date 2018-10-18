package com.soramitsu.test.repository

import android.arch.persistence.db.SupportSQLiteDatabase
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import com.soramitsu.test.domain.interfaces.Logger
import com.soramitsu.test.repository.datasource.api.ApiDataSource
import com.soramitsu.test.repository.datasource.api.ConnectivityInterceptor
import com.soramitsu.test.repository.datasource.api.WeatherApi
import com.soramitsu.test.repository.datasource.db.DB_NAME
import com.soramitsu.test.repository.datasource.db.DbDataSource
import com.soramitsu.test.repository.datasource.db.WeatherDao
import com.soramitsu.test.repository.datasource.db.WeatherDb
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
private const val HTTP_LOG_TAG = "soramitsu_http"
private const val NETWORK_TIMEOUT = 6 //seconds

val repositoryModule = Kodein.Module("Repository module") {
    bind<Retrofit>() with singleton { createRetrofit(instance(), instance()) }
    bind<WeatherApi>() with singleton { instance<Retrofit>().create(WeatherApi::class.java) }
    bind<ApiDataSource>() with singleton { ApiDataSource(instance(), instance()) }

    bind<WeatherDb>() with singleton { createDatabase(instance()) }
    bind<WeatherDao>() with singleton { instance<WeatherDb>().weatherDao() }
    bind<DbDataSource>() with singleton { DbDataSource(instance()) }

    bind<WeatherRepository>() with singleton { WeatherRepository(instance(), instance()) }
}

private fun createRetrofit(appContext: Context, logger: Logger): Retrofit {
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

    return Retrofit
        .Builder()
        .baseUrl(WEATHER_BASE_URL)
        .client(okHttpBuilder.build())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}

private fun createDatabase(appContext: Context) = Room
    .databaseBuilder(appContext, WeatherDb::class.java, DB_NAME)
    .addCallback(object : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            //todo реализовать
        }
    })
    .build()
