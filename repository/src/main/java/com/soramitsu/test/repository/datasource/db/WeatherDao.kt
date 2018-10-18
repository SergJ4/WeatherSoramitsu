package com.soramitsu.test.repository.datasource.db

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.soramitsu.test.repository.model.db.CITY_TABLE
import com.soramitsu.test.repository.model.db.City
import com.soramitsu.test.repository.model.db.CityWithWeather
import com.soramitsu.test.repository.model.db.WeatherForecast
import io.reactivex.Flowable

@Dao
interface WeatherDao {

    @Query("SELECT * from $CITY_TABLE")
    fun getCititesAndWeather(): Flowable<CityWithWeather>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrUpdateWeather(weather: WeatherForecast)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addCity(newCity: City)
}