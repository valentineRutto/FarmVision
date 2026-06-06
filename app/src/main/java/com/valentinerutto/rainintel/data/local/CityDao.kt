package com.valentinerutto.rainintel.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CityDao {
    @Query("SELECT COUNT(*) FROM cities")
    suspend fun count(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(cities: List<PreloadedCityEntity>)

    @Query(
        """
        SELECT * FROM cities
        WHERE city LIKE '%' || :query || '%' OR country LIKE '%' || :query || '%'
        ORDER BY city
        LIMIT :limit
        """
    )
    suspend fun search(query: String, limit: Int = 50): List<PreloadedCityEntity>
}
