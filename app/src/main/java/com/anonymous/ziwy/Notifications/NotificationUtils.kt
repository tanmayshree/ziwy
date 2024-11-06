package com.anonymous.ziwy.Notifications

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import com.anonymous.ziwy.Screens.HomeSection.ViewModel.MainStore
import com.anonymous.ziwy.Screens.HomeSection.ViewModel.MainViewModel
import java.util.Calendar

fun createNotificationChannel(context: Context, mainViewModel: MainViewModel, mainState: MainStore) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val channelId = "daily_notification_channel"
        val channelName = "Expiry check Notifications"
        val description = "Channel to check if coupon has expired"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(channelId, channelName, importance).apply {
            this.description = description
        }

        val notificationManager = context.getSystemService(NotificationManager::class.java)
        notificationManager.createNotificationChannel(channel)
    }
}

// Function to schedule the daily notification
fun scheduleDailyNotification(context: Context, mainViewModel: MainViewModel, mainState: MainStore) {
    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    scheduleNotification(context, alarmManager, 0, 23, 1)
    scheduleNotification(context, alarmManager, 16, 58, 2)
    scheduleNotification(context, alarmManager, 18, 58, 3)

}

@SuppressLint("ScheduleExactAlarm")
fun scheduleNotification(context: Context, alarmManager: AlarmManager, hours: Int, minutes: Int, requestCode: Int) {
    val intent = Intent(context, NotificationReceiver::class.java)
    val pendingIntent = PendingIntent.getBroadcast(
        context, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )

    // Set the alarm time based on parameters
    val calendar = Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, hours)
        set(Calendar.MINUTE, minutes)
        set(Calendar.SECOND, 0)

        // If the time has already passed today, schedule for the next day
        if (timeInMillis < System.currentTimeMillis()) {
            add(Calendar.DAY_OF_YEAR, 1)
        }
    }

    // Set exact alarm to fire at the specified time
    alarmManager.setExactAndAllowWhileIdle(
        AlarmManager.RTC_WAKEUP,
        calendar.timeInMillis,
        pendingIntent
    )
}
