package com.valentinerutto.farmvision.data

import com.valentinerutto.farmvision.data.local.WeatherDao
import com.valentinerutto.farmvision.data.local.WeatherEntity
import com.valentinerutto.farmvision.data.local.mapToDailyWeatherEntity
import com.valentinerutto.farmvision.data.local.toWeatherEntity
import com.valentinerutto.farmvision.data.models.WeatherUiData
import com.valentinerutto.farmvision.data.network.ApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class WeatherRepository(
    private val apiService: ApiService,
    private val weatherDao: WeatherDao
) {

    fun observeWeather(): Flow<WeatherUiData?> {
        return combine(
            weatherDao.observeCurrentLatest(),
            weatherDao.observeDaily()
        ) { currentWeather, dailyWeather ->
            currentWeather?.let {
                WeatherUiData(
                    currentWeather = it,
                    dailyWeather = dailyWeather
                )
            }
        }
    }

    suspend fun getWeather(lat: Double, lon: Double) {
        val currentWeather = weatherDao.getCurrentLatest()
        val dailyWeather = weatherDao.getDaily()

        if (currentWeather != null && dailyWeather.isNotEmpty() && currentWeather.isFresh()) {
            return
        }

        refreshWeather(lat, lon)
    }

    suspend fun refreshWeather(lat: Double, lon: Double) {
        val weatherResponse = apiService.getWeather(lat, lon)
        weatherDao.replaceWeather(
            currentWeather = weatherResponse.toWeatherEntity(),
            dailyWeather = mapToDailyWeatherEntity(weatherResponse)
        )
    }

    private fun WeatherEntity.isFresh(): Boolean {
        val cachedDate = time.substringBefore("T")
        val today = SimpleDateFormat(WEATHER_DATE_PATTERN, Locale.US).format(Date())

        return cachedDate == today
    }


}

private const val WEATHER_DATE_PATTERN = "yyyy-MM-dd"
