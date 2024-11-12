package com.anonymous.ziwy.Screens.RootComponent.Components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.anonymous.ziwy.R
import com.anonymous.ziwy.Utilities.ZColors.orange

@Composable
fun SplashPage() {
    Box(
        modifier = Modifier.fillMaxSize()
            .background(orange),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painterResource(id = R.drawable.ziwy_logo1),
            contentDescription = "Home",
            modifier = Modifier
                .size(180.dp)
        )
    }
}