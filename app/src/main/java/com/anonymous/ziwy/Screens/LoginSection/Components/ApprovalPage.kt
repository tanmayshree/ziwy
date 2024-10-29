package com.anonymous.ziwy.Screens.LoginSection.Components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.anonymous.ziwy.Screens.LoginSection.ViewModel.LoginStore
import com.anonymous.ziwy.Screens.LoginSection.ViewModel.LoginViewModel
import com.anonymous.ziwy.Utilities.ZColors.lightBlueTransparent
import com.anonymous.ziwy.Utilities.ZColors.orange
import com.anonymous.ziwy.Utilities.ZColors.white

@Composable
fun ApprovalPage(
    loginNavController: NavHostController,
    viewModel: LoginViewModel,
    state: LoginStore,
    navController: NavHostController
) {

    /*LaunchedEffect(key1 = state.isLoginSuccess) {
        if (state.isLoginSuccess == true) {
            println("620555 - LoginPage.kt - LoginPage - isLoginSuccess - true - navigate to MainPage")
            navController.navigate(NavigationItem.MainPage.route) {
                popUpTo(navController.graph.startDestinationId) {
                    inclusive = true
                }
                launchSingleTop = true
            }
        }*//* else if (state.isLoginSuccess == false) {
            loginNavController.popBackStack()
        }*//*
    }*/

    Surface(
        color = lightBlueTransparent
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 10.dp, start = 16.dp, end = 16.dp, bottom = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            CircularProgressIndicator(
                modifier = Modifier.size(60.dp),
                strokeWidth = 5.dp,
                color = orange
            )

            Text(
                text = "Please check your WhatsApp/SMS for Login Approval.",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = orange,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 50.dp, vertical = 16.dp),
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 30.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .offset(y = 5.dp)
                            .shadow(10.dp, RoundedCornerShape(40.dp))
                            .clip(RoundedCornerShape(40.dp))
                    )
                    Surface(
                        color = orange,
                        shape = RoundedCornerShape(40.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .shadow(5.dp, RoundedCornerShape(40.dp))
                            .clip(RoundedCornerShape(40.dp))
                            .clickable {
                                loginNavController.popBackStack()
                            }
                    ) {
                        Text(
                            text = "Back to Login Page", fontSize = 24.sp,
                            modifier = Modifier.padding(
                                start = 10.dp,
                                top = 12.dp,
                                bottom = 13.dp,
                                end = 10.dp
                            ),
                            textAlign = TextAlign.Center,
                            color = white,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

//                Button(
//                    onClick = { viewModel.setLoginStatus(
//                        isLoading = false,
//                        isLoginSuccess = true,
//                        successResponse = null
//                    ) }
//                ) {
//                    Text("Go to Home Page -> Testing only")
//                }
            }
        }
    }
}