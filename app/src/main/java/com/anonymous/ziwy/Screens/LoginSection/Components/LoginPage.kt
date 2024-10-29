package com.anonymous.ziwy.Screens.LoginSection.Components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.anonymous.ziwy.Screens.LoginSection.ViewModel.LoginStore
import com.anonymous.ziwy.Screens.LoginSection.ViewModel.LoginViewModel
import com.anonymous.ziwy.Screens.RootComponent.NavigationItem

@Composable
fun LoginPage(
    navController: NavHostController,
    viewModel: LoginViewModel,
    state: LoginStore
) {

    val loginNavController = rememberNavController()

    LaunchedEffect(key1 = state.isNewUser) {
        if (state.isNewUser == true) {
            println("620555 - LoginPage.kt - LoginPage - isNewUser - true - navigate to AddUserInformationPage")
            navController.navigate(NavigationItem.AddUserInformationPage.route) {
//                popUpTo(navController.graph.startDestinationId) {
//                    inclusive = true
//                }
//                launchSingleTop = true
            }
        } else if (state.isNewUser == false) {
            //navigate to main page
            println("620555 - LoginPage.kt - LoginPage - isNewUser - false - navigate to MainPage")
            navController.navigate(NavigationItem.MainPage.route) {
                popUpTo(NavigationItem.LoginPage.route) {
                    inclusive = true
                }
            }

        }
    }
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
//                .padding(8.dp)
        ) {
            NavHost(
                navController = loginNavController,
                startDestination = NavigationItem.EnterMobileNumber.route
            ) {
                composable(NavigationItem.EnterMobileNumber.route) {
                    EnterMobileNumber(loginNavController, viewModel, state)
                }
                composable(NavigationItem.ApprovalPage.route) {
                    ApprovalPage(loginNavController, viewModel, state, navController)
                }
            }
        }
    }
}