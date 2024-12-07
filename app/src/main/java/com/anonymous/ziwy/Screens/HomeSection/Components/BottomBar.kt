package com.anonymous.ziwy.Screens.HomeSection.Components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.anonymous.ziwy.R
import com.anonymous.ziwy.Screens.RootComponent.NavigationItem
import com.anonymous.ziwy.Utilities.ZColors.black
import com.anonymous.ziwy.Utilities.ZColors.lightBlue
import com.anonymous.ziwy.Utilities.ZColors.orange

@Composable
fun BottomBar(navController: NavHostController) {
    val currentDestination = navController.currentBackStackEntryAsState().value?.destination?.route

    HorizontalDivider()

    Row(
        modifier = Modifier
            .height(75.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        BottomBarItem(
            iconId = R.drawable.home_icon,
            selectedIconId = R.drawable.home_icon_selected,
            label = "Home",
            isSelected = currentDestination == NavigationItem.HomePage.route,
            selectedColor = orange, // Example orange color
            onClick = {
                if (currentDestination != NavigationItem.HomePage.route) {
                    navController.navigate(NavigationItem.HomePage.route) {
                        popUpTo(navController.graph.startDestinationId) {
                            inclusive = false
                        }
                        launchSingleTop = true
                    }
                }
            }
        )

        BottomBarItem(
            iconId = R.drawable.upload_icon_grey,
            selectedIconId = R.drawable.upload_icon,
            label = "Upload",
            isSelected = currentDestination == NavigationItem.UploadPage.route,
            selectedColor = lightBlue,
            onClick = {
                if (currentDestination != NavigationItem.UploadPage.route) {
                    navController.navigate(NavigationItem.UploadPage.route) {
                        popUpTo(navController.graph.startDestinationId) {
                            inclusive = false
                        }
                        launchSingleTop = true
                    }
                }
            }
        )

        BottomBarItem(
            iconId = R.drawable.profile_icon,
            selectedIconId = R.drawable.profile_icon_selected,
            label = "Profile",
            isSelected = currentDestination == NavigationItem.ProfilePage.route,
            selectedColor = orange,
            onClick = {
                if (currentDestination != NavigationItem.ProfilePage.route) {
                    navController.navigate(NavigationItem.ProfilePage.route) {
                        popUpTo(navController.graph.startDestinationId) {
                            inclusive = false
                        }
                        launchSingleTop = true
                    }
                }
            }
        )
    }
}

@Composable
fun BottomBarItem(
    iconId: Int,
    selectedIconId: Int,
    label: String,
    isSelected: Boolean,
    selectedColor: Color,
    onClick: () -> Unit
) {
    val iconToShow = if (isSelected) selectedIconId else iconId
    val textColor = if (isSelected) selectedColor else black

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable(onClick = onClick)
    ) {
        Image(
            painter = painterResource(id = iconToShow),
            contentDescription = label,
            modifier = Modifier.size(24.dp)
        )
        Text(
            text = label,
            fontSize = 12.sp,
            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium,
            color = textColor
        )
    }
}




