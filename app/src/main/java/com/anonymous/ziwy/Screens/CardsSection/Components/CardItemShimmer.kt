package com.anonymous.ziwy.Screens.CardsSection.Components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.anonymous.ziwy.Screens.CardsSection.Models.Card
import com.anonymous.ziwy.Utilities.Utils
import com.anonymous.ziwy.Utilities.ZColors.blue
import com.anonymous.ziwy.Utilities.ZColors.grey
import com.anonymous.ziwy.Utilities.ZColors.lightBlueTransparent
import com.anonymous.ziwy.Utilities.ZColors.white

@Composable
fun CardItemShimmer() {
    Surface(
        color = Utils.transitionColor(),
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier
            .fillMaxWidth(),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
                .height(48.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "",
                fontSize = 16.sp,
                modifier = Modifier.padding(horizontal = 10.dp)
                    .fillMaxWidth(),
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center,
                color = white
            )
        }
    }
}