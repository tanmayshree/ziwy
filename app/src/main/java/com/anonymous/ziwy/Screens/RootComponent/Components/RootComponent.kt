package com.anonymous.ziwy.Screens.RootComponent.Components

import android.content.Intent
import android.widget.Toast
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.anonymous.ziwy.GenericModels.NotificationIntentData
import com.anonymous.ziwy.MainActivity
import com.anonymous.ziwy.Screens.CouponDetailSection.Components.CouponDetailPage
import com.anonymous.ziwy.Screens.HomeSection.Components.MainPage
import com.anonymous.ziwy.Screens.LoginSection.Components.AddUserInformationPage
import com.anonymous.ziwy.Screens.LoginSection.Components.LoginPage
import com.anonymous.ziwy.Screens.LoginSection.ViewModel.LoginStore
import com.anonymous.ziwy.Screens.LoginSection.ViewModel.LoginViewModel
import com.anonymous.ziwy.Screens.RootComponent.NavigationItem
import com.anonymous.ziwy.Screens.RootComponent.RootUtils.getStartDestination
import com.anonymous.ziwy.Screens.RootComponent.RootUtils.onLogin
import com.anonymous.ziwy.Screens.RootComponent.RootUtils.onLogout
import com.anonymous.ziwy.Screens.TourSection.Components.TourPage
import kotlinx.coroutines.launch

@Composable
fun RootComponent(viewModel: LoginViewModel, state: LoginStore, intent: Intent) {

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

        // navigate to coupon detail screen with couponId
        val notificationIntentData =
            intent.getParcelableExtra<NotificationIntentData>("notificationIntentData")
        if (notificationIntentData != null) {
            println("620555 - RootComponent.kt - notificationIntentData - $notificationIntentData")
            navController.navigate(NavigationItem.CouponDetailPage.route + "/${notificationIntentData.contextId}")
        }
    }

    LaunchedEffect(key1 = state.message) {
        if (state.message.isNullOrEmpty()) return@LaunchedEffect
        Toast.makeText(context as MainActivity, state.message, Toast.LENGTH_LONG).show()
        viewModel.clearMessage()
    }

    LaunchedEffect(key1 = state.isLoginSuccess) {
        if (state.isLoginSuccess == true) {
//            println("620555 - RootComponent.kt - isLoginSuccess - true - navigate to MainPage")
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
        startDestination = NavigationItem.SplashPage.route, // Start with a splash screen
        enterTransition = {
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = tween(700)
            )
        },
        exitTransition = {
            slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = tween(700)
            )
        },
        popEnterTransition = {
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = tween(700)
            )
        },
        popExitTransition = {
            slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = tween(700)
            )
        }
    ) {
        composable(NavigationItem.SplashPage.route) {
            SplashPage()
        }
        composable(NavigationItem.LoginPage.route) {
            LoginPage(navController = navController, viewModel, state)
        }
        composable(NavigationItem.MainPage.route) {
            MainPage(
                navController = navController,
                imageUri = state.imageUri,
                isGoogleSignInCompleted = state.isGoogleSignInCompleted
            ) {
                coroutineScope.launch {
                    onLogout(context)
//                    println("620555 RootComponent - logout - navigate to LoginPage")
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

        //coupon detail screen with coupon id
        composable(
            route = NavigationItem.CouponDetailPage.route + "/{couponId}",
            arguments = listOf(
                navArgument("couponId") {
                    type = NavType.StringType
                    nullable = true
                }
            )
        ) { backStackEntry ->
            val couponId = backStackEntry.arguments?.getString("couponId")
            CouponDetailPage(navController, couponId)
        }
    }
}
