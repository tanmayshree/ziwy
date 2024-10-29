package com.anonymous.ziwy.Screens.TourSection

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.anonymous.ziwy.Screens.RootComponent.NavigationItem
import com.anonymous.ziwy.Utilities.ZColors.white

@Composable
fun TourPage(
    navController: NavController
) {

    val tourNavController = rememberNavController()
    Scaffold(
        containerColor = white
//        containerColor = orange
    ) {
        Box(
            modifier = Modifier
                .padding(it)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            NavHost(
                navController = tourNavController,
                startDestination = NavigationItem.TourScreen1.route,
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
                composable(NavigationItem.TourScreen1.route) {
                    TourScreen1(
                        tourNavController = tourNavController,
                        mainNavController = navController
                    )
                }
                composable(NavigationItem.TourScreen2.route) {
                    TourScreen2(
                        tourNavController = tourNavController,
                        mainNavController = navController
                    )
                }
                composable(NavigationItem.TourScreen3.route) {
                    TourScreen3(
                        tourNavController = tourNavController,
                        mainNavController = navController
                    )
                }
            }
        }
    }
}