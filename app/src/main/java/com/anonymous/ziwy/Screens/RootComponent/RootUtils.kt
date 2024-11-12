package com.anonymous.ziwy.Screens.RootComponent

import android.content.Context
import com.anonymous.ziwy.MainActivity
import com.anonymous.ziwy.Screens.LoginSection.ViewModel.LoginStore
import com.anonymous.ziwy.Screens.LoginSection.ViewModel.LoginViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

object RootUtils {
    // Suspend function remains the same
    suspend fun getStartDestination(
        context: Context,
        viewModel: LoginViewModel,
        state: LoginStore,
        coroutineScope: CoroutineScope
    ): String {
        val startDestination: String

        val token = withContext(Dispatchers.IO) {
            (context as MainActivity).readFromPreferences("token")
        }

        val username = withContext(Dispatchers.IO) {
            (context as MainActivity).readFromPreferences("username")
        }

        println("620555 - RootComponent.kt - token - $token")

        startDestination = if (token.isNullOrEmpty()) {
            NavigationItem.LoginPage.route
        } else {
            NavigationItem.MainPage.route
        }

        println("620555 - RootComponent.kt - startDestination - $startDestination")
        return startDestination
    }

    suspend fun onLogout(
        context: Context
    ) {
        withContext(Dispatchers.IO) {
            (context as MainActivity).saveToPreferences("token", "")
            (context as MainActivity).saveToPreferences("username", "")
            (context as MainActivity).saveToPreferences("countryCode", "")
            (context as MainActivity).saveToPreferences("phoneNumber", "")
            (context as MainActivity).saveToPreferences("joiningDate", "")
        }
    }

    suspend fun onLogin(
        context: Context,
        token: String,
        username: String,
        countryCode: String,
        phoneNumber: String,
        joiningDate: String?
    ) {
        withContext(Dispatchers.IO) {
            (context as MainActivity).saveToPreferences("token", token)
            (context as MainActivity).saveToPreferences("username", username)
            (context as MainActivity).saveToPreferences("countryCode", countryCode)
            (context as MainActivity).saveToPreferences("phoneNumber", phoneNumber)
            joiningDate?.let { (context as MainActivity).saveToPreferences("joiningDate", it) }
            delay(2000)
        }
    }
}