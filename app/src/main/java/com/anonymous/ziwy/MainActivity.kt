package com.anonymous.ziwy

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Telephony
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.anonymous.ziwy.GenericComponents.ForceUpdateDialog
import com.anonymous.ziwy.Notifications.PermissionManager
import com.anonymous.ziwy.Notifications.createNotificationChannel
import com.anonymous.ziwy.Notifications.scheduleDailyNotification
import com.anonymous.ziwy.Screens.LoginSection.Models.LoginRequestModel
import com.anonymous.ziwy.Screens.LoginSection.ViewModel.LoginViewModel
import com.anonymous.ziwy.Screens.LoginSection.ViewModel.LoginViewModelFactory
import com.anonymous.ziwy.Screens.RootComponent.Components.RootComponent
import com.anonymous.ziwy.Utilities.Utils
import com.anonymous.ziwy.Utilities.Utils.handleGoogleSignInIntent
import com.anonymous.ziwy.Utilities.Utils.handleSharingIntents
import com.anonymous.ziwy.Utilities.ZColors.white
import com.anonymous.ziwy.WorkManager.DailySMSWorker
import com.anonymous.ziwy.ui.theme.ZiwyTheme
import com.otpless.dto.HeadlessRequest
import com.otpless.dto.HeadlessResponse
import com.otpless.main.OtplessManager
import com.otpless.main.OtplessView
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import java.util.Calendar
import java.util.concurrent.TimeUnit

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class MainActivity : ComponentActivity() {

    private lateinit var otplessView: OtplessView

    private lateinit var viewModel: LoginViewModel

    private lateinit var imageUris: Uri

    private val googleSignInComplete: MutableState<Boolean?> = mutableStateOf(null)

    // For Notifications
    private lateinit var permissionManager: PermissionManager

//    private val dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        permissionManager = PermissionManager(this)

        otplessView = OtplessManager.getInstance().getOtplessView(this)
        otplessView.initHeadless("195ZDBMFRHBVQ5NFI1KE")
        otplessView.setHeadlessCallback(this::onHeadlessCallback)

        setContent {
            ZiwyTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    containerColor = white
                ) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .padding(innerPadding)
//                            .background(white)
                    ) {

                        viewModel = viewModel<LoginViewModel>(
                            key = "LoginViewModel",
                            factory = LoginViewModelFactory()
                        )

                        val state = viewModel.state.collectAsState().value

                        LaunchedEffect(key1 = imageUris) {
                            imageUris.let {
                                viewModel.setImageUri(it)
                            }
                        }

                        LaunchedEffect(key1 = googleSignInComplete.value) {
                            println("620555 Google sign in complete: ${googleSignInComplete.value}")
                            if (googleSignInComplete.value == true) {
                                Toast.makeText(
                                    this@MainActivity,
                                    "Google sign-in success. Coupons in your e-mail will be added to Ziwy.",
                                    Toast.LENGTH_LONG
                                ).show()
                                viewModel.setGoogleSignInCompleted(isGoogleSignInCompleted = true)
                                delay(5000)
                                googleSignInComplete.value = null
                            }
                        }

                        val isDialogVisible = remember { mutableStateOf(true) }
                        LaunchedEffect(key1 = Unit) {
                            viewModel.getAppUpdateInfo()
                        }
                        LaunchedEffect(key1 = state.appUpdateInfo) {
                            isDialogVisible.value = state.appUpdateInfo?.let {
                                Utils.isForceUpdateDialogVisible(
                                    appUpdateInfoResponseModel = it,
                                    context = this@MainActivity
                                )
                            } ?: false
                        }

                        ForceUpdateDialog(isDialogVisible = isDialogVisible)
                        RootComponent(viewModel, state, intent)

                        // Notifications
                        val context = LocalContext.current
                        LaunchedEffect(Unit) {

                            if (ActivityCompat.checkSelfPermission(
                                    context,
                                    Manifest.permission.READ_SMS
                                ) != PackageManager.PERMISSION_GRANTED
                            ) {
                                smsPermissionLauncher.launch(Manifest.permission.READ_SMS)
                            } else {
                                scheduleDailyTask()
                            }

                            /*if (ActivityCompat.checkSelfPermission(
                                    context,
                                    Manifest.permission.RECEIVE_SMS
                                ) != PackageManager.PERMISSION_GRANTED
                            ) {
                                smsPermissionLauncher.launch(Manifest.permission.RECEIVE_SMS)
                            } else {
                                receiveSMS()
                            }*/

                            if (!permissionManager.hasNotificationPermission()) {
                                notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                                //permissionManager.requestNotificationPermission(this@MainActivity)
                            } else {
                                if (permissionManager.hasExactAlarmPermission()) {
                                    createNotificationChannel(context)
                                    scheduleDailyNotification(context)
                                } else {
                                    permissionManager.requestExactAlarmPermission(this@MainActivity)
                                    permissionManager.requestNotificationPermission(this@MainActivity)
                                }
                            }

                        }
                    }
                }
            }
        }
        imageUris = handleSharingIntents(intent)
        googleSignInComplete.value = handleGoogleSignInIntent(intent)
    }

    suspend fun saveToPreferences(key: String, value: String) {
        val dataStoreKey = stringPreferencesKey(key)
        dataStore.edit { user ->
            user[dataStoreKey] = value
        }

        println("620555 Saved to preferences: $key: $value")
    }

    suspend fun readFromPreferences(key: String): String? {
        val dataStoreKey = stringPreferencesKey(key)
        val preferences = dataStore.data.first()
//        println("620555 Read from preferences: $key: ${preferences[dataStoreKey]}")
        return preferences[dataStoreKey]
    }

    fun startLogin(loginRequestModel: LoginRequestModel) {
//        println("620555 Start login with phone number: ${loginRequestModel.phoneNumber}")
        val request = HeadlessRequest()
        request.setPhoneNumber("91", loginRequestModel.phoneNumber)
        otplessView.startHeadless(request, this::onHeadlessCallback)
    }

    private fun onHeadlessCallback(response: HeadlessResponse) {
        if (response.statusCode == 200) {
            when (response.responseType) {
                "INITIATE" -> {
                    // notify that headless authentication has been initiated
                    val responseWithToken = response.response
//                    println("620555 Headless authentication initiated: $responseWithToken")
                    viewModel.setLoginStatus(
                        isLoading = true,
                        isLoginSuccess = false,
                        successResponse = null
                    )
                }

                "VERIFY" -> {
                    // notify that verification is completed
                    // and this is notified just before "ONETAP" final response
                    val responseWithToken = response.response
//                    println("620555 Verification completed: $responseWithToken")
                }

                "OTP_AUTO_READ" -> {
                    val otp = response.response?.optString("otp")
                    // auto read otp
//                    println("620555 Auto read otp: $otp")
                }

                "ONETAP" -> {
                    // final response with token
                    val responseWithToken = response.response
//                    println("620555 Final response with token: $responseWithToken")
//                    viewModel.setLoginStatus(
//                        isLoading = false,
//                        isLoginSuccess = true,
//                        successResponse = responseWithToken?.toString()
//                    )

                    viewModel.getUserData(responseWithToken?.toString() ?: "")

//                    lifecycleScope.launch {
//                        saveToPreferences("token", responseWithToken?.toString() ?: "")
//                        saveToPreferences("countryCode", viewModel.state.value.countryCode.value)
//                        saveToPreferences("phoneNumber", viewModel.state.value.phoneNumber.value)
//                    }
                }

                else -> {
                    // else case
                    val responseWithToken = response.response
//                    println("620555 ELse response: ${response.responseType} $responseWithToken")
                }
            }

        } else {
            // handle error
            val error = response.response?.optString("errorMessage")
//            println("620555 Error: $error")
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        otplessView?.onNewIntent(intent)
        imageUris = handleSharingIntents(intent)

        googleSignInComplete.value = handleGoogleSignInIntent(intent)
    }

    override fun onBackPressed() {
        otplessView?.let {
            if (it.onBackPressed()) return
        }
        super.onBackPressed()
    }

    private val notificationPermissionLauncher = registerForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            // Notification permission granted
            if (permissionManager.hasExactAlarmPermission()) {
                createNotificationChannel(this)
                scheduleDailyNotification(this)
            } else {
                permissionManager.requestExactAlarmPermission(this)
            }
        } else {
            // Notification permission denied
            // Handle accordingly
        }
    }

    private val smsPermissionLauncher = registerForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            // SMS permission granted
            println("620555 SMS permission granted")
            scheduleDailyTask()
        } else {
            // SMS permission denied
            // Handle accordingly
            println("620555 SMS permission denied")
            //call again
        }
    }

    private fun receiveSMS() {
        val smsBroadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                println("620555 SMS Received")
                Telephony.Sms.Intents.getMessagesFromIntent(intent).forEachIndexed { index, sms ->
                    // display everything
                    println("620555 SMS Received: $index: ${sms.displayMessageBody}")
                    println("620555 SMS Received: $index: ${sms.messageBody}")
                    //from where the sms is coming
                    println("620555 SMS Received: $index: ${sms.displayOriginatingAddress}")
                    println("620555 SMS Received: $index: ${sms.originatingAddress}")
                }
            }
        }
        registerReceiver(
            smsBroadcastReceiver,
            IntentFilter("android.provider.Telephony.SMS_RECEIVED")
        )
    }

    private fun scheduleDailyTask() {
        val currentTime = Calendar.getInstance()
        val targetTime = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 20)
            set(Calendar.MINUTE, 45)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)

            if (before(currentTime)) {
                add(Calendar.DAY_OF_MONTH, 1) // Schedule for the next day if the time has passed
            }
        }

        val initialDelay = targetTime.timeInMillis - currentTime.timeInMillis

        val workRequest = PeriodicWorkRequestBuilder<DailySMSWorker>(24, TimeUnit.HOURS)
            .setInitialDelay(initialDelay, TimeUnit.MILLISECONDS)
            .setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED) // Optional: Add constraints
                    .build()
            )
            .build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "DailySMSWorker",
            ExistingPeriodicWorkPolicy.UPDATE,
            workRequest
        )
    }
//    override fun onNewIntent(intent: Intent) {
//        if (otplessView != null) {
//            otplessView.onNewIntent(intent)
//        }
//
//        super.onNewIntent(intent)
//    }

    /*    override fun onNewIntent(intent: Intent) {
            super.onNewIntent(intent)
            if (otplessView != null) {
                otplessView.onNewIntent(intent)
            }
    //        setIntent(intent)
    //        imageUris = handleSharingIntents(intent)
    //        val newIntent = Intent(this, MainActivity::class.java).apply {
    //            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
    //        }
    //        startActivity(newIntent)
    //        finish()
        }



        @Deprecated("This method has been deprecated in favor of using the\n      {@link OnBackPressedDispatcher} via {@link #getOnBackPressedDispatcher()}.\n      The OnBackPressedDispatcher controls how back button events are dispatched\n      to one or more {@link OnBackPressedCallback} objects.")
        override fun onBackPressed() {
            // Make sure you call this code before super.onBackPressed();
            if (otplessView.onBackPressed()) return

            super.onBackPressed()
        }*/


}