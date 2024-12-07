package com.anonymous.ziwy.Screens.HomeSection.Components

/*
import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.anonymous.ziwy.Screens.HomeSection.ViewModel.MainStore
import com.anonymous.ziwy.Utilities.ZColors.blue
import com.anonymous.ziwy.Utilities.ZColors.lightBlueTransparent
import com.anonymous.ziwy.Utilities.ZColors.white

@Composable
fun SignInToGoogleStrip(state: MainStore) {

    val uriHandler = LocalUriHandler.current

    val isEnabled = remember { mutableStateOf(false) }

    val context = LocalContext.current
    val authUrl =
        "https://ziwy.shop/auth?mobileNumber=${state.phoneNumber}&countryCode=${state.countryCode}"

    LaunchedEffect(isEnabled.value) {
        if (!isEnabled.value) return@LaunchedEffect
        val customTabsIntent = CustomTabsIntent.Builder()
            .setShowTitle(true)
            .setInstantAppsEnabled(true)
            .build()

        customTabsIntent.launchUrl(context, Uri.parse(authUrl))
        isEnabled.value = false
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 10.dp),
        colors = CardColors(
            containerColor = white,
            contentColor = blue,
            disabledContainerColor = white,
            disabledContentColor = blue
        ),
        elevation = CardDefaults.elevatedCardElevation()
    ) {
        Surface(
            color = lightBlueTransparent,
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp))
                .clickable {
//                    uriHandler.openUri("https://ziwy.shop/auth?mobileNumber=${state.phoneNumber}&countryCode=${state.countryCode}")
                    isEnabled.value = true
                },
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp)
                    .height(44.dp)
            ) {
                Text(
                    text = "Tap to import offers from Gmail!",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = blue,
                    modifier = Modifier.padding(end = 0.dp)
                )
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = "Arrow Icon",
                    modifier = Modifier.size(26.dp),
                    tint = blue
                )
            }
        }
    }
}*/
