package com.valentinerutto.farmvision.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "weather_table")
data class WeatherEntity(
    @PrimaryKey val id: Int,
)
