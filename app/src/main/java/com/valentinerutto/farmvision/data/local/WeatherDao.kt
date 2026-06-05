package com.valentinerutto.farmvision.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface WeatherDao {

        @Query("SELECT * FROM weather_table")
        fun observeCurrentLatest(): Flow<WeatherEntity?>

       @Query("SELECT * FROM daily_table")
       fun observeDaily(): Flow<List<DailyWeatherEntity>>
        @Insert(onConflict = OnConflictStrategy.REPLACE)
        suspend fun upsert(entity: WeatherEntity)
    }

