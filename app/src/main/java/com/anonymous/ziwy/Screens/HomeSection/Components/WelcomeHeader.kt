package com.anonymous.ziwy.Screens.HomeSection.Components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.anonymous.ziwy.Screens.HomeSection.ViewModel.MainStore

@Composable
fun WelcomeHeader(state: MainStore) {
    Text(
        text = "Welcome, ${state.username ?: "User"}!", fontSize = 20.sp,
        modifier = Modifier
            .padding(horizontal = 14.dp),
        fontWeight = FontWeight.SemiBold,
    )
}