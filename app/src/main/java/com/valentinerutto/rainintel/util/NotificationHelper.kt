package com.valentinerutto.rainintel.util

import android.Manifest
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.valentinerutto.rainintel.MainActivity
import com.valentinerutto.rainintel.R

class WeatherNotificationHelper(
    private val context: Context
) {
    fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return

        val channel = NotificationChannel(
            WEATHER_ALERT_CHANNEL_ID,
            WEATHER_ALERT_CHANNEL_NAME,
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = "Weather alerts for rain and severe conditions"
        }

        val notificationManager = context.getSystemService(NotificationManager::class.java)
        notificationManager.createNotificationChannel(channel)
    }

    @SuppressLint("MissingPermission")
    fun showWeatherAlert(alert: WeatherAlert) {
        if (!canPostNotifications()) return

        val contentIntent = PendingIntent.getActivity(
            context,
            alert.notificationId,
            Intent(context, MainActivity::class.java).apply {
                action = ACTION_OPEN_WEATHER_ALERT
                putExtra(EXTRA_NOTIFICATION_ID, alert.notificationId)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            },
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(
            context,
            WEATHER_ALERT_CHANNEL_ID
        )
            .setSmallIcon(R.drawable.ic_notification_weather)
            .setLargeIcon(context.drawableToBitmap(R.drawable.ic_sun_cloud))
            .setContentTitle(alert.title)
            .setContentText(alert.message)
            .setStyle(NotificationCompat.BigTextStyle().bigText(alert.message))
            .setContentIntent(contentIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()

        NotificationManagerCompat.from(context).notify(
            alert.notificationId,
            notification
        )
    }

    fun showSimulatedWeatherAlerts() {
        createNotificationChannel()
        showWeatherAlert(
            WeatherAlert(
                notificationId = SIMULATED_UV_NOTIFICATION_ID,
                title = "☀️ High UV Alert",
                message = "UV index will reach 8.4 at 1 PM in Nairobi. Wear sunscreen and avoid long sun exposure."
            )
        )
        showWeatherAlert(
            WeatherAlert(
                notificationId = SIMULATED_RAIN_NOTIFICATION_ID,
                title = "🌧 Rain Expected",
                message = "There is a 75% chance of rain at 4 PM today. Carry an umbrella."
            )
        )
    }

    private fun canPostNotifications(): Boolean {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU ||
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
    }

    companion object {
        const val WEATHER_ALERT_CHANNEL_ID = "weather_alerts"
        const val ACTION_OPEN_WEATHER_ALERT = "com.valentinerutto.rainintel.OPEN_WEATHER_ALERT"
        const val EXTRA_NOTIFICATION_ID = "extra_notification_id"
        const val UNKNOWN_NOTIFICATION_ID = -1
        private const val WEATHER_ALERT_CHANNEL_NAME = "Weather alerts"
        private const val SIMULATED_UV_NOTIFICATION_ID = 2101
        private const val SIMULATED_RAIN_NOTIFICATION_ID = 2102
    }
}

private fun Context.drawableToBitmap(drawableResId: Int): Bitmap? {
    val drawable = ContextCompat.getDrawable(this, drawableResId) ?: return null
    val width = drawable.intrinsicWidth.takeIf { it > 0 } ?: 128
    val height = drawable.intrinsicHeight.takeIf { it > 0 } ?: 128
    val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    drawable.setBounds(0, 0, canvas.width, canvas.height)
    drawable.draw(canvas)
    return bitmap
}
