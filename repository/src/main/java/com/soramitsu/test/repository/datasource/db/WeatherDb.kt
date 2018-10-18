package com.soramitsu.test.repository.datasource.db

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.soramitsu.test.repository.model.db.City
import com.soramitsu.test.repository.model.db.WeatherForecast

const val DB_NAME = "weather_db"

@Database(version = 1, entities = [City::class, WeatherForecast::class])
abstract class WeatherDb : RoomDatabase() {
    abstract fun weatherDao(): WeatherDao
}