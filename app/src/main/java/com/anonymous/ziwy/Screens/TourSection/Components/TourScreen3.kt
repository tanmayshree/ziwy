package com.anonymous.ziwy.Screens.TourSection.Components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.anonymous.ziwy.GenericComponents.TourSlidingLeftArrow
import com.anonymous.ziwy.R
import com.anonymous.ziwy.Utilities.ZColors.orange
import com.anonymous.ziwy.Utilities.ZColors.white

@Composable
fun TourScreen3(tourNavController: NavHostController, mainNavController: NavController) {
    Scaffold(
        containerColor = white,
        topBar = {
            Text(
                text = "Bravo! Now never miss out on a deal!",
                color = orange,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .padding(start = 20.dp, top = 50.dp, end = 20.dp, bottom = 50.dp)
            )
        },
        bottomBar = {
            Row(
                horizontalArrangement = Arrangement.End,
                modifier = Modifier.fillMaxWidth()
            ) {
                //skip button
                Text(
                    text = "Go to Home Page >>",
                    color = orange,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(22.dp)
//                    .alpha(0.8f)
                        .clickable {
                            mainNavController.popBackStack()
                        }
                )
            }
        }
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .verticalScroll(rememberScrollState())
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp, 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TourSlidingLeftArrow(
                    modifier = Modifier
                ) {
                    tourNavController.popBackStack()
                }
                Image(
                    painter = painterResource(id = R.drawable.tour_screen_3_image),
                    contentDescription = "Tour Screen 1 Image",
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 5.dp)
                )
                Box(modifier = Modifier.size(40.dp))
            }
        }
    }
}