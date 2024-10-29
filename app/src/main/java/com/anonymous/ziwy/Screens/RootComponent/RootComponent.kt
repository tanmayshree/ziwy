package com.anonymous.ziwy.Screens.RootComponent

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.anonymous.ziwy.MainActivity
import com.anonymous.ziwy.Screens.HomeSection.Components.MainPage
import com.anonymous.ziwy.Screens.LoginSection.Components.AddUserInformationPage
import com.anonymous.ziwy.Screens.LoginSection.Components.LoginPage
import com.anonymous.ziwy.Screens.LoginSection.ViewModel.LoginStore
import com.anonymous.ziwy.Screens.LoginSection.ViewModel.LoginViewModel
import com.anonymous.ziwy.Screens.TourSection.TourPage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun RootComponent(viewModel: LoginViewModel, state: LoginStore) {

    val navController = rememberNavController()
    val context = LocalContext.current
//    val imageUris = remember { mutableStateOf<List<Uri>?>(null) }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        val startDestination = getStartDestination(context, viewModel, state, coroutineScope)
//        val startDestination = NavigationItem.MainPage.route

        navController.navigate(startDestination) {
            popUpTo(NavigationItem.SplashPage.route) { inclusive = true }
            launchSingleTop = true
        }
    }

    LaunchedEffect(key1 = state.message) {
        if (state.message.isNullOrEmpty()) return@LaunchedEffect
        Toast.makeText(context as MainActivity, state.message, Toast.LENGTH_SHORT).show()
    }

    LaunchedEffect(key1 = state.isLoginSuccess) {
        if (state.isLoginSuccess == true) {
            println("620555 - RootComponent.kt - isLoginSuccess - true - navigate to MainPage")
            navController.navigate(NavigationItem.MainPage.route) {
                popUpTo(NavigationItem.LoginPage.route) {
                    inclusive = true
                }
                launchSingleTop = true
            }

            if (state.isNewUser == true)
                navController.navigate(NavigationItem.TourPage.route)

            onLogin(
                context = context,
                token = state.token,
                username = state.userData?.userName ?: "User",
                countryCode = state.countryCode.value,
                phoneNumber = state.phoneNumber.value,
                joiningDate = state.userData?.creationTime
            )
        }/* else if (state.isLoginSuccess == false) {
            loginNavController.popBackStack()
        }*/
    }

    NavHost(
        navController = navController,
        startDestination = NavigationItem.SplashPage.route // Start with a splash screen
    ) {
        composable(NavigationItem.SplashPage.route) {
            SplashPage()
        }
        composable(NavigationItem.LoginPage.route) {
            LoginPage(navController = navController, viewModel, state)
        }
        composable(NavigationItem.MainPage.route) {
            MainPage(navController = navController, imageUri = state.imageUri) {
                coroutineScope.launch {
                    onLogout(context)
                    println("620555 RootComponent - logout - navigate to LoginPage")
//                    println("620555 RootComponent - navGraph stack - ${navController.currentBackStack.value}")
                    navController.popBackStack()
                    navController.navigate(NavigationItem.LoginPage.route) {
//                        popUpTo(NavigationItem.MainPage.route) { inclusive = true }
                        launchSingleTop = true
                    }
                    viewModel.clearStates()
                }
            }
        }
        composable(NavigationItem.AddUserInformationPage.route) {
            AddUserInformationPage(navController, viewModel, state)
        }
        //TourPage
        composable(NavigationItem.TourPage.route) {
            TourPage(navController)
        }
    }
}

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

suspend fun onLogout(context: Context) {
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