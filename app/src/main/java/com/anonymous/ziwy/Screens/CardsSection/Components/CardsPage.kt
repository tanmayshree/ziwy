package com.anonymous.ziwy.Screens.CardsSection.Components

import android.app.Application
import android.widget.Toast
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.anonymous.ziwy.MainActivity
import com.anonymous.ziwy.Screens.CardsSection.ViewModel.CardsViewModel
import com.anonymous.ziwy.Screens.CardsSection.ViewModel.CardsViewModelFactory
import com.anonymous.ziwy.Screens.RootComponent.NavigationItem

@Composable
fun CardsPage(navController: NavHostController) {

    val cardsNavController = rememberNavController()

    val context = LocalContext.current
    val application = context.applicationContext as Application

    val viewModel = viewModel<CardsViewModel>(
        key = "CardsViewModel",
        factory = CardsViewModelFactory(application)
    )

    val state = viewModel.state.collectAsState().value

    LaunchedEffect(Unit) {
        viewModel.getUserData(context)
    }


    LaunchedEffect(key1 = state.message) {
        if (state.message.isNullOrEmpty()) return@LaunchedEffect
        Toast.makeText(context as MainActivity, state.message, Toast.LENGTH_LONG).show()
        viewModel.clearMessage()
    }
    /*LaunchedEffect(state.loadingScreenState?.isLoading, state.loadingScreenState?.screen) {
        if (state.loadingScreenState?.isLoading == false) {
            when (state.loadingScreenState.screen) {
                ZConstants.FETCH_CARDS_LIST -> {
                    if (state.userCardsList.isNullOrEmpty()) {
                        navController.navigate(NavigationItem.MyCardsPage.route)
                    }
                }
            }
        }
    }*/

    LaunchedEffect(state.isFetchingCardListSuccess) {
        if(state.isFetchingCardListSuccess == true) {
            if (state.cardsList?.filter { it.isSelected.value }.isNullOrEmpty()) {
                cardsNavController.navigate(NavigationItem.AddCardsPage.route) {
                    launchSingleTop = true
                }
            }
        } else if (state.isFetchingCardListSuccess == false) {
            navController.popBackStack()
            Toast.makeText(context, "Please try again!", Toast.LENGTH_SHORT).show()
        }
    }

    LaunchedEffect(state.isCardsUpdatedSuccessfully) {
        if (state.isCardsUpdatedSuccessfully == true) {
            navController.popBackStack()
        }
    }

    NavHost(
        navController = cardsNavController,
        startDestination = NavigationItem.MyCardsPage.route,
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
    ){
        composable(NavigationItem.MyCardsPage.route) {
            MyCardsPage(viewModel, state, cardsNavController, navController)
        }
        composable(NavigationItem.AddCardsPage.route) {
            AddCardsPage(viewModel, state, cardsNavController)
        }
    }
}