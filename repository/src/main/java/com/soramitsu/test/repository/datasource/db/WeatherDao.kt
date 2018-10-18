package com.soramitsu.test.repository.datasource.db

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.soramitsu.test.repository.model.db.*
import io.reactivex.Flowable

@Dao
interface WeatherDao {

    @Query("SELECT * FROM $CITY_TABLE")
    fun getCititesAndWeather(): Flowable<CityWithWeather>

    @Query("SELECT * FROM $CITY_TABLE WHERE $CITY_NAME_COLUMN LIKE :cityName")
    fun getWeatherForCity(cityName: String): Flowable<CityWithWeather>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrUpdateWeather(weather: WeatherForecast)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addCity(newCity: City)
}