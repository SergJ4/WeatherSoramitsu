package com.soramitsu.test.repository.model.db

import android.arch.persistence.room.*
import android.arch.persistence.room.ForeignKey.CASCADE

const val FORECAST_TABLE = "forecasts"
const val FORECAST_CODE_COLUMN = "code"
const val FORECAST_DAY_COLUMN = "week_day"
const val FORECAST_WIND_COLUMN = "wind_speed"
const val FORECAST_DESCRIPTION_COLUMN = "description"
const val FORECAST_TEMPERATURE_COLUMN = "temperature"
const val FORECAST_HUMIDITY_COLUMN = "humidity"
const val FORECAST_PRESSURE_COLUMN = "pressure"
const val FORECAST_ICON_COLUMN = "icon"
const val FORECAST_CITY_ID_COLUMN = "city_id"

@Entity(
    tableName = FORECAST_TABLE,
    foreignKeys = [ForeignKey(
        entity = City::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("city_id"),
        onDelete = CASCADE
    )]
)
data class WeatherForecast(
    @ColumnInfo(name = FORECAST_CODE_COLUMN)
    val code: Int,

    @TypeConverters(ForecastDayConverter::class)
    @ColumnInfo(name = FORECAST_DAY_COLUMN)
    @PrimaryKey
    val forDay: ForecastDay,

    @ColumnInfo(name = FORECAST_DESCRIPTION_COLUMN)
    val description: String,

    @ColumnInfo(name = FORECAST_TEMPERATURE_COLUMN)
    val temperature: Float,

    @ColumnInfo(name = FORECAST_HUMIDITY_COLUMN)
    val humidity: Float,

    @ColumnInfo(name = FORECAST_PRESSURE_COLUMN)
    val pressure: Float,

    @ColumnInfo(name = FORECAST_WIND_COLUMN)
    val windSpeed: Float,

    @ColumnInfo(name = FORECAST_ICON_COLUMN)
    val icon: String,

    @ColumnInfo(name = FORECAST_CITY_ID_COLUMN)
    @PrimaryKey
    val forCityId: Long
) {

    enum class ForecastDay(val code: Int) {
        CURRENT_WEATHER(0), TOMORROW(1), AFTER_TOMORROW(2), THIRD_DAY(3)
    }
}

class ForecastDayConverter {
    companion object {

        @JvmStatic
        @TypeConverter
        fun toDay(code: Int): WeatherForecast.ForecastDay = when (code) {
            0 -> WeatherForecast.ForecastDay.CURRENT_WEATHER
            1 -> WeatherForecast.ForecastDay.TOMORROW
            2 -> WeatherForecast.ForecastDay.AFTER_TOMORROW
            3 -> WeatherForecast.ForecastDay.THIRD_DAY
            else -> throw IllegalArgumentException("Unknow day code: $code")
        }

        @JvmStatic
        @TypeConverter
        fun fromDay(day: WeatherForecast.ForecastDay): Int = day.code
    }
}