package com.valentinerutto.farmvision.data.network

interface ApiService {
    suspend fun getWeatherForecast()
}