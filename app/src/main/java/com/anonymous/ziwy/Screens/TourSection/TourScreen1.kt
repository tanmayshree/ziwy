package com.anonymous.ziwy.Screens.TourSection

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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.anonymous.ziwy.GenericComponents.TourSlidingRightArrow
import com.anonymous.ziwy.R
import com.anonymous.ziwy.Screens.RootComponent.NavigationItem
import com.anonymous.ziwy.Utilities.ZColors.orange
import com.anonymous.ziwy.Utilities.ZColors.white

@Composable
fun TourScreen1(tourNavController: NavHostController, mainNavController: NavController) {

    Scaffold(
        containerColor = white,
        topBar = {
            Text(
                text = "Got a coupon? Take a quick screenshot!",
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
                    text = "Skip Tour >>",
                    color = orange,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(20.dp)
                        .alpha(0.8f)
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
                //hide visibility of the arrow

                Box(modifier = Modifier.size(40.dp))
                Image(
                    painter = painterResource(id = R.drawable.tour_screen_1_image),
                    contentDescription = "Tour Screen 1 Image",
                    modifier = Modifier.weight(1f)
                )
                TourSlidingRightArrow(
                    modifier = Modifier
                ) {
                    tourNavController.navigate(NavigationItem.TourScreen2.route)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun text() {
    TourScreen1(
        tourNavController = rememberNavController(),
        mainNavController = rememberNavController()
    )
}