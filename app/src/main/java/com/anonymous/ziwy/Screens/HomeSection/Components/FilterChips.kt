package com.anonymous.ziwy.Screens.HomeSection.Components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.anonymous.ziwy.R
import com.anonymous.ziwy.Utilities.ZColors
import com.anonymous.ziwy.Utilities.ZColors.black
import com.anonymous.ziwy.Utilities.ZColors.blue
import com.anonymous.ziwy.Utilities.ZColors.transparent
import com.anonymous.ziwy.Utilities.ZColors.white

@Composable
fun FilterChips(
    isExpiringSoonFilterEnabled: MutableState<Boolean>,
    isProductsFilterEnabled: MutableState<Boolean>,
    onProductFilterClick: () -> Unit,
    isBrandsFilterEnabled: MutableState<Boolean>,
    onBrandFilterClick: () -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 20.dp, horizontal = 10.dp)
    ) {
        Row(
            modifier = Modifier
                .clickable {
                    isExpiringSoonFilterEnabled.value = !isExpiringSoonFilterEnabled.value
                }
                .border(
                    width = 1.dp,
                    color = if (isExpiringSoonFilterEnabled.value) blue else black,
                    shape = RoundedCornerShape(40.dp)
                )
                .background(
                    color = if (isExpiringSoonFilterEnabled.value) blue else transparent,
                    shape = RoundedCornerShape(40.dp)
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Expiring Soon",
                fontSize = 14.sp,
                modifier = Modifier.padding(
                    start = 12.dp, top = 4.dp, bottom = 5.dp, end = 12.dp
                ),
                color = if (isExpiringSoonFilterEnabled.value) white else black,
                fontWeight = FontWeight.Medium
            )
        }

        Row(
            modifier = Modifier
                .clickable {
//                    isProductsFilterEnabled.value = !isProductsFilterEnabled.value
                    onProductFilterClick.invoke()
                }
                .border(
                    width = 1.dp,
                    color = if (isProductsFilterEnabled.value) ZColors.orange else black,
                    shape = RoundedCornerShape(40.dp)
                )
                .background(
                    color = if (isProductsFilterEnabled.value) ZColors.orange else transparent,
                    shape = RoundedCornerShape(40.dp)
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Products",
                fontSize = 14.sp,
                modifier = Modifier.padding(
                    start = 12.dp, top = 4.dp, bottom = 5.dp, end = 3.dp
                ),
                color = if (isProductsFilterEnabled.value) white else black,
                fontWeight = FontWeight.Medium
            )
            Image(
                painterResource(id = R.drawable.filter_icon),
                contentDescription = "Home",
                modifier = Modifier.size(16.dp),
                colorFilter = ColorFilter.tint(if (isProductsFilterEnabled.value) white else black)
            )
            Spacer(modifier = Modifier.size(12.dp))
        }

        Row(
            modifier = Modifier
                .clickable {
//                    isBrandsFilterEnabled.value = !isBrandsFilterEnabled.value
                    onBrandFilterClick.invoke()
                }
                .border(
                    width = 1.dp,
                    color = if (isBrandsFilterEnabled.value) ZColors.orange else black,
                    shape = RoundedCornerShape(40.dp)
                )
                .background(
                    color = if (isBrandsFilterEnabled.value) ZColors.orange else transparent,
                    shape = RoundedCornerShape(40.dp)
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Brands",
                fontSize = 14.sp,
                modifier = Modifier.padding(
                    start = 12.dp, top = 4.dp, bottom = 5.dp, end = 3.dp
                ),
                color = if (isBrandsFilterEnabled.value) white else black,
                fontWeight = FontWeight.Medium
            )
            Image(
                painterResource(id = R.drawable.filter_icon),
                contentDescription = "Home",
                modifier = Modifier.size(16.dp),
                colorFilter = ColorFilter.tint(if (isBrandsFilterEnabled.value) white else black)
            )
            Spacer(modifier = Modifier.size(12.dp))
        }
    }
}