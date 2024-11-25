package com.anonymous.ziwy.Screens.CardsSection.Components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
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
import com.anonymous.ziwy.Utilities.ZColors.blue
import com.anonymous.ziwy.Utilities.ZColors.lightBlueTransparent
import com.anonymous.ziwy.Utilities.ZColors.white

@Composable
fun CardItemWithDelete(card: Card) {

    Surface(
        color = if (card.isSelected.value) blue else lightBlueTransparent,
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier
            .fillMaxWidth(),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = card.cardName ?: "NA",
                fontSize = 16.sp,
                modifier = Modifier
                    .padding(horizontal = 10.dp)
                    .fillMaxWidth(.8f),
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center,
                color = if (card.isSelected.value) white else blue
            )

            //delete icon
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Delete",
                modifier = Modifier
                    .weight(1f)
                    .height(24.dp)
                    .clickable {
                        card.isSelected.value = false
                    },
                tint = if (card.isSelected.value) white else blue
            )
        }
    }
}