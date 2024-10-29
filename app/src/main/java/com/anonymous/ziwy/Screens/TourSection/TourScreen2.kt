package com.anonymous.ziwy.Screens.TourSection

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.anonymous.ziwy.GenericComponents.TourSlidingLeftArrow
import com.anonymous.ziwy.GenericComponents.TourSlidingRightArrow
import com.anonymous.ziwy.R
import com.anonymous.ziwy.Screens.RootComponent.NavigationItem
import com.anonymous.ziwy.Utilities.ZColors.orange

@Composable
fun TourScreen2(tourNavController: NavHostController, mainNavController: NavController) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxSize()
    ) {

        Column {
            Text(
                text = "Share it with Ziwy!",
                color = orange,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, top = 50.dp, end = 20.dp, bottom = 50.dp)
            )

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
                    painter = painterResource(id = R.drawable.tour_screen_2_image),
                    contentDescription = "Tour Screen 1 Image",
                    modifier = Modifier.weight(1f)
                )
                TourSlidingRightArrow(
                    modifier = Modifier
                ) {
                    tourNavController.navigate(NavigationItem.TourScreen3.route)
                }
            }
        }

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
}