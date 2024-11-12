package com.anonymous.ziwy.Screens.HomeSection.Components

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ExitToApp
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavHostController
import com.anonymous.ziwy.R
import com.anonymous.ziwy.Screens.HomeSection.ViewModel.MainStore
import com.anonymous.ziwy.Screens.HomeSection.ViewModel.MainViewModel
import com.anonymous.ziwy.Utilities.ZColors.grey

@Composable
fun ProfilePage(
    navController: NavHostController,
    viewModel: MainViewModel,
    state: MainStore,
    onLogout: () -> Unit
) {

    val uriHandler = LocalUriHandler.current

    Column(
        modifier = Modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            contentAlignment = Alignment.TopCenter,
            modifier = Modifier
                .fillMaxWidth(),
        ) {

            Box(
                modifier = Modifier
                    .offset(y = (5).dp)
//                    .clipToBounds()
                    .zIndex(1f),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.guest_profile_image),
                    contentDescription = "",
                    modifier = Modifier
//                        .background(Color.Green, CircleShape)
//                        .clip(CircleShape)
                        .size(120.dp)
//                        .padding(10.dp)
                )
            }

            Box(modifier = Modifier) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 60.dp, start = 12.dp, bottom = 12.dp, end = 12.dp)
                        .border(
                            width = 1.dp,
                            color = grey,
                            shape = RoundedCornerShape(16.dp)
                        ),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.size(60.dp))

                    Text(
                        text = state.username ?: "User", fontSize = 20.sp,
                        modifier = Modifier.padding(12.dp, 5.dp),
                        fontWeight = FontWeight.SemiBold,
                    )
                    state.joiningDate?.let {
                        Text(
                            text = "Joined on $it", fontSize = 14.sp,
                            modifier = Modifier.padding(12.dp, 5.dp),
                            fontWeight = FontWeight.Medium,
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    val activeCoupons = state.couponsList.filter { it.redeemed != true }.size
                    val redeemedCoupons = state.couponsList.size - activeCoupons
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(text = activeCoupons.toString())
                            Text(text = "Active Coupons")
                        }
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(text = redeemedCoupons.toString())
                            Text(text = "Redeemed")
                        }
//                        Column(
//                            horizontalAlignment = Alignment.CenterHorizontally
//                        ) {
//                            Text(text = "9")
//                            Text(text = "Cards")
//                        }
                    }
                }
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .clickable {
                    uriHandler.openUri("https://www.ziwy.in/#/contact")
                },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Outlined.Delete,
                contentDescription = "Home",
                modifier = Modifier
                    .padding(vertical = 12.dp)
                    .size(24.dp),
            )
            Text(
                text = "Delete your data",
                fontSize = 18.sp,
                modifier = Modifier
                    .padding(start = 10.dp)
                    .padding(vertical = 12.dp)
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .clickable {
                    uriHandler.openUri("https://www.ziwy.in/#/contact")
//                    openUrlInBrowser("https://www.ziwy.in/#/contact", context)
                },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Outlined.Info,
                contentDescription = "Home",
                modifier = Modifier
                    .padding(vertical = 12.dp)
                    .size(24.dp),
            )
            Text(
                text = "Help & Support",
                fontSize = 18.sp,
                modifier = Modifier
                    .padding(start = 10.dp)
                    .padding(vertical = 12.dp)
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .clickable {
                    onLogout.invoke()
                },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.AutoMirrored.Outlined.ExitToApp,
                contentDescription = "Home",
                modifier = Modifier
                    .padding(vertical = 12.dp)
                    .size(24.dp),
            )
            Text(
                text = "Logout",
                fontSize = 18.sp,
                modifier = Modifier
                    .padding(start = 10.dp)
                    .padding(vertical = 12.dp)
            )
        }
    }


}
