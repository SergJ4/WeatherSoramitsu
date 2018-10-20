package com.soramitsu.test.repository.datasource.db

import android.arch.persistence.room.*
import com.soramitsu.test.repository.model.db.*
import io.reactivex.Flowable
import io.reactivex.Single

@Dao
interface WeatherDao {

    @Query("SELECT * FROM $CITY_TABLE")
    @Transaction
    fun getCititesAndWeather(): Flowable<List<CityWithWeather>>

    @Query("SELECT * FROM $CITY_TABLE WHERE $CITY_NAME_COLUMN LIKE :cityName")
    @Transaction
    fun getWeatherForCity(cityName: String): Flowable<CityWithWeather>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrUpdateWeather(weather: WeatherForecast)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addCities(vararg cities: City)

    @Query("SELECT * FROM $CITY_TABLE")
    fun getAllCities(): Single<List<City>>
}