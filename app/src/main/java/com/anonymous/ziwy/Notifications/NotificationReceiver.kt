package com.anonymous.ziwy.Notifications

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.anonymous.ziwy.GenericModels.NotificationIntentData
import com.anonymous.ziwy.MainActivity
import com.anonymous.ziwy.R
import com.anonymous.ziwy.Screens.HomeSection.Models.Coupon
import com.anonymous.ziwy.Utilities.Retrofit.Repository
import com.anonymous.ziwy.Utilities.Retrofit.Resource
import com.anonymous.ziwy.Utilities.Utils
import com.anonymous.ziwy.Utilities.ZConstants
import com.anonymous.ziwy.dataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Calendar

class NotificationReceiver : BroadcastReceiver() {

    private val repository = Repository()

    override fun onReceive(context: Context, intent: Intent) {

        val requestCode = intent.getIntExtra("requestCode", 0)

        val permissionManager = PermissionManager(context)

        if (permissionManager.hasNotificationPermission()) {

            CoroutineScope(Dispatchers.Main).launch {
                val userDetails = getUserDetailsFromPreferences(context.dataStore)

                val couponResource = fetchCouponsList(
                    mobileNumber = userDetails.phoneNumber,
                    countryCode = userDetails.countryCode
                )

                couponResource.filter { it.redeemed != true }.forEachIndexed { index, it ->
                    it.expiryDate?.let { date ->
                        Utils.checkDateDifference(date)?.let { daysDifference ->
                            println("620555 expiry $daysDifference")

                            when {
                                (daysDifference < 0L) -> {
                                    println("620555 NotificationReceiver Coupon has expired")
                                }

                                (daysDifference <= 1L) -> {
                                    showNotification(
                                        context = context,
                                        expiryPrompt = "Your ${it.couponBrand} coupon is expiring in 1 day. Click now to redeem.",
                                        notificationId = index,
                                        requestCode = requestCode,
                                        couponId = it.couponID
                                    )
                                }

                                (daysDifference <= 2L) -> {
                                    showNotification(
                                        context = context,
                                        expiryPrompt = "Your ${it.couponBrand} coupon is expiring in 2 days. Click now to redeem.",
                                        notificationId = index,
                                        requestCode = requestCode,
                                        couponId = it.couponID
                                    )
                                }

                                (daysDifference <= 5L) -> {
                                    showNotification(
                                        context = context,
                                        expiryPrompt = "Your ${it.couponBrand} coupon is expiring in 5 days. Click now to redeem.",
                                        notificationId = index,
                                        requestCode = requestCode,
                                        couponId = it.couponID
                                    )
                                }

                                else -> {
                                    println("620555 NotificationReceiver Coupon is not expiring soon")
                                }
                            }
                        }
                    }
                }
                rescheduleForNextDay(context, intent)
            }

        } else {
            Toast.makeText(context, "Notification Permission Denied", Toast.LENGTH_SHORT).show()
        }
    }

    @SuppressLint("MissingPermission")
    private fun showNotification(
        context: Context,
        expiryPrompt: String,
        notificationId: Int,
        requestCode: Int,
        couponId: String?
    ) {
        val channelId = "daily_notification_channel"

        // Create an intent to open MainActivity when the notification is clicked
        val notificationIntent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra(
                "notificationIntentData",
                NotificationIntentData(
                    context = ZConstants.EXPIRY_NOTIFICATION,
                    contextId = couponId
                )
            )
        }

        // Create a pending intent
        val pendingIntent = PendingIntent.getActivity(
            context,
            notificationId,
            notificationIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ziwy_logo2)
            .setContentTitle("Looking for some discounts?")
            .setContentText(expiryPrompt)  // Show Prompt here
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(context)) {
            notify(notificationId, builder.build())
        }
    }

    @SuppressLint("ScheduleExactAlarm")
    private fun rescheduleForNextDay(context: Context, intent: Intent) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val requestCode = intent.getIntExtra("requestCode", 0)
        val hours = intent.getIntExtra("hours", 0)
        val minutes = intent.getIntExtra("minutes", 0)

        println("620555 NotificationReceiver Rescheduling for next day at $hours:$minutes,$requestCode")
        // Set the calendar for the next day at the same time
        val calendar = Calendar.getInstance().apply {
            add(Calendar.DAY_OF_YEAR, 1)  // Move to the next day
            set(Calendar.HOUR_OF_DAY, hours)
            set(Calendar.MINUTE, minutes)
            set(Calendar.SECOND, 0)
        }

        val rescheduleIntent = Intent(context, NotificationReceiver::class.java).apply {
            putExtra("hours", hours)
            putExtra("minutes", minutes)
            putExtra("requestCode", requestCode)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            /* context = */ context,
            /* requestCode = */ requestCode,
            /* intent = */ rescheduleIntent,
            /* flags = */ PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Schedule the exact time for the next day
        alarmManager.setExactAndAllowWhileIdle(
            /* type = */ AlarmManager.RTC_WAKEUP,
            /* triggerAtMillis = */ calendar.timeInMillis,
            /* operation = */ pendingIntent
        )
    }

    suspend fun fetchCouponsList(mobileNumber: String?, countryCode: String?): List<Coupon> {
        var newList: List<Coupon> = emptyList()

        withContext(Dispatchers.IO) {
            repository.getCouponsList(mobileNumber, countryCode).collect { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        println("Loading...")  // You can add logging if needed
                    }

                    is Resource.Success -> {
                        val couponsList = resource.data?.body?.map {
                            it.copy(
                                expiryStatus = it.expiryDate?.let { date ->
                                    Utils.checkDateDifference(date)?.let { daysDifference ->
                                        when {
                                            (daysDifference < 0L) -> ZConstants.COUPON_HAS_EXPIRED
                                            (daysDifference <= 5L) -> ZConstants.COUPON_IS_EXPIRING_SOON
                                            else -> ZConstants.COUPON_IS_VALID
                                        }
                                    } ?: ZConstants.COUPON_IS_VALID
                                } ?: ZConstants.COUPON_IS_VALID
                            )
                        } ?: emptyList()

                        newList = couponsList
                        println("620555 NotificationReceiver Success ${resource.data}")
                    }

                    is Resource.Error -> {
                        println("620555 NotificationReceiver Error ${resource.message}")
                    }
                }
            }
        }

        println("620555 NotificationReceiver NewList $newList")
        return newList
    }

}