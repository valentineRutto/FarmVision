package com.valentinerutto.farmvision.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [WeatherEntity::class], version = 1, exportSchema = false)
abstract class FarmVisionDatabase: RoomDatabase() {

    abstract fun farmVisionDao(): FarmVisionDao

    companion object Companion {
        @Volatile
        private var INSTANCE: FarmVisionDatabase? = null
        fun getDatabase(context: Context): FarmVisionDatabase {

            return INSTANCE ?: synchronized(this) {

                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    FarmVisionDatabase::class.java,
                    "farmvision_database"
                              ).allowMainThreadQueries()
                .fallbackToDestructiveMigration(false).build()

                INSTANCE = instance
                instance
            }
        }
    }
}
