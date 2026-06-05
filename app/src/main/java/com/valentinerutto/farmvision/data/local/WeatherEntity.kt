package com.valentinerutto.farmvision.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "weather_table")
data class WeatherEntity(
@PrimaryKey val id: String = "latest",
    val location: String,
    val temperature: Double,
    val condition: String,
    val humidity: Int,
    val windKph: Double,
    val rainChance: Int,
    val aiSummary: String,
    val forecastJson: String,
    val updatedAt: Long = System.currentTimeMillis()
)
