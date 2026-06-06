package com.valentinerutto.rainintel.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "cities")
data class PreloadedCityEntity(
    @PrimaryKey val id: Long,
    val city: String,
    val lat: Double,
    val lng: Double,
    val country: String
)
