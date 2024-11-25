package com.anonymous.ziwy.Screens.HomeSection.Components

import android.app.Application
import android.net.Uri
import android.widget.Toast
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.anonymous.ziwy.MainActivity
import com.anonymous.ziwy.Screens.HomeSection.Models.ExtractCouponImageRequestModel
import com.anonymous.ziwy.Screens.HomeSection.ViewModel.MainViewModel
import com.anonymous.ziwy.Screens.HomeSection.ViewModel.MainViewModelFactory
import com.anonymous.ziwy.Screens.RootComponent.NavigationItem
import com.anonymous.ziwy.Utilities.Utils.fileUriToBase64
import com.anonymous.ziwy.Utilities.ZColors.transparent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun MainPage(
    navController: NavHostController,
    imageUri: Uri?,
    onLogout: () -> Unit
) {

    val mainNavController = rememberNavController()

    val context = LocalContext.current
    val application = context.applicationContext as Application

    val viewModel = viewModel<MainViewModel>(
        key = "MainViewModel",
        factory = MainViewModelFactory(application)
    )

    val state = viewModel.state.collectAsState().value

    LaunchedEffect(key1 = imageUri) {
        if (imageUri != null && imageUri != Uri.EMPTY) {
            CoroutineScope(Dispatchers.IO).launch {
                println("620555 Image: $imageUri")
                viewModel.extractCouponImage(
                    context = context,
                    imageUri = imageUri,
                    payload = ExtractCouponImageRequestModel(
                        fileUriToBase64(
                            uri = imageUri,
                            resolver = context.contentResolver
                        )
                    )
                )
            }
        }
    }

    LaunchedEffect(key1 = Unit) {
        viewModel.getUserData(context)
    }

    LaunchedEffect(key1 = state.message) {
        if (state.message.isNullOrEmpty()) return@LaunchedEffect
        Toast.makeText(context as MainActivity, state.message, Toast.LENGTH_LONG).show()
        viewModel.clearMessage()
    }

    Scaffold(
        bottomBar = {
            BottomBar(
                navController = mainNavController,
                onCardSectionClick = {
                    navController.navigate(NavigationItem.CardsPage.route)
                }
            )
        },
        containerColor = transparent,
    ) { it ->
        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
//                    .padding(horizontal = 8.dp)
            ) {
                NavHost(
                    navController = mainNavController,
                    startDestination = NavigationItem.HomePage.route,
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
                    composable(NavigationItem.HomePage.route) {
                        HomePage(
                            navController = mainNavController,
                            viewModel = viewModel,
                            state = state,
                            onCouponClick = { couponId ->
                                navController.navigate(NavigationItem.CouponDetailPage.route + "/$couponId")
                            }
                        )
                    }
                    composable(NavigationItem.UploadPage.route) {
                        UploadPage(mainNavController, viewModel, state)
                    }
                    composable(NavigationItem.ProfilePage.route) {
                        ProfilePage(mainNavController, viewModel, state, onLogout)
                    }
                }
            }
        }
    }

}