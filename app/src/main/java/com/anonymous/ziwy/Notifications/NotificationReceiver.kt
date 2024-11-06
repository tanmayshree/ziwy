package com.anonymous.ziwy.Notifications

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.anonymous.ziwy.MainActivity
import com.anonymous.ziwy.R
import com.anonymous.ziwy.Screens.HomeSection.Models.Coupon
import com.anonymous.ziwy.Utilities.Retrofit.Repository
import com.anonymous.ziwy.Utilities.Retrofit.Resource
import com.anonymous.ziwy.Utilities.Utils
import com.anonymous.ziwy.Utilities.ZConstants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Calendar

class NotificationReceiver : BroadcastReceiver() {

    private val repository = Repository()
    //private lateinit var couponList: List<Coupon>


    override fun onReceive(context: Context, intent: Intent) {
        val permissionManager = PermissionManager(context)
        if (permissionManager.hasNotificationPermission()) {


//            val mobileNumber = "7667484399"
//            val countryCode = "91"

            CoroutineScope(Dispatchers.IO).launch {
//                val userDetails = Utils.getUserDetailsFromPreferences(context)

                val couponResource = fetchCouponsList("6205556957", "91")

                couponResource.forEach {
                    it.expiryDate?.let { date ->
                        Utils.checkDateDifference(date)?.let { daysDifference ->
                            println("620555 expiry $daysDifference")
                            when {
                                (daysDifference <= 1L) -> {
                                    showNotification(
                                        context,
                                        "Your ${it.couponBrand} coupon is expiring in 1 day. Click now to redeem."
                                    )
                                }

                                (daysDifference <= 2L) -> {
                                    showNotification(
                                        context,
                                        "Your ${it.couponBrand} coupon is expiring in 2 days. Click now to redeem."
                                    )
                                }

                                (daysDifference <= 5L) -> {
                                    showNotification(
                                        context,
                                        "Your ${it.couponBrand} coupon is expiring in 5 days. Click now to redeem."
                                    )
                                }

                                else -> {
                                    showNotification(
                                        context,
                                        "later on expire"
                                    )
                                }
                            }
                        } ?: ZConstants.COUPON_IS_VALID
                    }
                }
//                withContext(Dispatchers.Main) {
//                    if (processCouponsToCheckExpiry(couponResource)) {
//                        print("meranaamsurajhai:::::::: got the expiry prompt")
//                        val expiryPrompt = "Your coupons will expire soon! Claim them now to take advantage of the discounts."
//                        showNotification(context, expiryPrompt)
//                        rescheduleForNextDay(context, intent)
//                    }
//                }
            }

        } else {
            Toast.makeText(context, "Notification Permission Denied", Toast.LENGTH_SHORT).show()
        }
    }

    private fun processCouponsToCheckExpiry(couponListResponse: List<Coupon>): Boolean {

        val coupons = couponListResponse
        val currentTime = Calendar.getInstance().timeInMillis
        val twoDaysLater = currentTime + (2 * 24 * 60 * 60 * 1000)

        for (coupon in coupons) {
            val expiryDate = coupon.expiryDate
            if (expiryDate != null) {
                // Convert expiry date to timestamp (in milliseconds)
                val expiryTimestamp = convertDateToTimestamp(expiryDate)
                if (expiryTimestamp != null && expiryTimestamp in currentTime..twoDaysLater) {
                    print("meranaamsurajhai:::::::: TRUE ${coupon}")
                    return true
                }
            }
        }
        print("meranaamsurajhai:::::::: FALSE")
        return false
    }

    private fun convertDateToTimestamp(expiryDate: String): Long? {
        return try {
            val dateFormat = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault())
            val date = dateFormat.parse(expiryDate)
            date?.time
        } catch (e: Exception) {
            Log.e("NotificationReceiver", "Error parsing expiry date: $expiryDate", e)
            null
        }
    }

    @SuppressLint("MissingPermission")
    private fun showNotification(context: Context, expiryPrompt: String) {
        val channelId = "daily_notification_channel"
        val notificationId = 1

        print("meranaamsurajhai:::::::: received expiry prompt")

        // Create an intent to open MainActivity when the notification is clicked
        val notificationIntent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        // Create a pending intent
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            notificationIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ziwy_logo)
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

        // Set the calendar for the next day at the same time
        val calendar = Calendar.getInstance().apply {
            add(Calendar.DAY_OF_YEAR, 1)
            set(Calendar.HOUR_OF_DAY, get(Calendar.HOUR_OF_DAY))
            set(Calendar.MINUTE, get(Calendar.MINUTE))
            set(Calendar.SECOND, 0)
        }

        val rescheduleIntent = Intent(context, NotificationReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            requestCode,
            rescheduleIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Schedule the exact time for the next day
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            pendingIntent
        )
    }

    suspend fun fetchCouponsList(mobileNumber: String?, countryCode: String?): List<Coupon> {
        var newList: List<Coupon> = emptyList()
        CoroutineScope(Dispatchers.Main).launch {
            repository.getCouponsList(mobileNumber, countryCode).collect { resource ->
                when (resource) {
                    is Resource.Loading -> {

                        println("620555 MainViewModel fetchCouponsList Loading...")
                        Log.d("meranaamsurajhai", "loading api response ${resource.data}")
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

                        println("620555 MainViewModel fetchCouponsList Success ${resource.data}")
                        Log.d("meranaamsurajhai", "got api response ${resource.data}")
                    }

                    is Resource.Error -> {
                        println("620555 MainViewModel fetchCouponsList Error ${resource.message}")
                        Log.d("meranaamsurajhai", "failed api response ${resource.data}")
                    }

                }
            }
        }

        return newList

    }
}