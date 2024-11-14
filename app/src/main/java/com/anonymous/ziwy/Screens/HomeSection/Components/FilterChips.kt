package com.anonymous.ziwy.Screens.HomeSection.Components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
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
import com.anonymous.ziwy.Utilities.ZColors.darkGrey
import com.anonymous.ziwy.Utilities.ZColors.orange
import com.anonymous.ziwy.Utilities.ZColors.transparent
import com.anonymous.ziwy.Utilities.ZColors.white

@Composable
fun FilterChips(
    isExpiringSoonFilterEnabled: MutableState<Boolean>,
    isProductsFilterEnabled: MutableState<Boolean>,
    onProductFilterClick: () -> Unit,
    selectedProductFilter: MutableState<String?>,
    isBrandsFilterEnabled: MutableState<Boolean>,
    onBrandFilterClick: () -> Unit,
    selectedBrandFilter: MutableState<String?>,
    isActiveFilterEnabled: MutableState<Boolean>,
    isRedeemedFilterEnabled: MutableState<Boolean>
) {
    val scrollState = rememberScrollState()
    Row(
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(scrollState)
            .padding(vertical = 20.dp, horizontal = 20.dp)
    ) {
        Row(
            modifier = Modifier
                .clickable {
                    isExpiringSoonFilterEnabled.value = !isExpiringSoonFilterEnabled.value
                    if (isExpiringSoonFilterEnabled.value) {
                        isActiveFilterEnabled.value = false
                        isRedeemedFilterEnabled.value = false
                        selectedProductFilter.value = null
                        isProductsFilterEnabled.value = false
                        selectedBrandFilter.value = null
                        isBrandsFilterEnabled.value = false
                    }
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

        Spacer(modifier = Modifier.size(16.dp))

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

        Spacer(modifier = Modifier.size(16.dp))

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

        Spacer(modifier = Modifier.size(16.dp))

        Row(
            modifier = Modifier
                .clickable {
                    isActiveFilterEnabled.value = !isActiveFilterEnabled.value
                    if (isActiveFilterEnabled.value) {
                        isRedeemedFilterEnabled.value = false
                        isExpiringSoonFilterEnabled.value = false
                        selectedProductFilter.value = null
                        isProductsFilterEnabled.value = false
                        selectedBrandFilter.value = null
                        isBrandsFilterEnabled.value = false
                    }
                }
                .border(
                    width = 1.dp,
                    color = if (isActiveFilterEnabled.value) orange else black,
                    shape = RoundedCornerShape(40.dp)
                )
                .background(
                    color = if (isActiveFilterEnabled.value) orange else transparent,
                    shape = RoundedCornerShape(40.dp)
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Active",
                fontSize = 14.sp,
                modifier = Modifier.padding(
                    start = 12.dp, top = 4.dp, bottom = 5.dp, end = 12.dp
                ),
                color = if (isActiveFilterEnabled.value) white else black,
                fontWeight = FontWeight.Medium
            )
        }

        Spacer(modifier = Modifier.size(16.dp))

        Row(
            modifier = Modifier
                .clickable {
                    isRedeemedFilterEnabled.value = !isRedeemedFilterEnabled.value
                    if (isRedeemedFilterEnabled.value) {
                        isExpiringSoonFilterEnabled.value = false
                        isActiveFilterEnabled.value = false
                        selectedProductFilter.value = null
                        isProductsFilterEnabled.value = false
                        selectedBrandFilter.value = null
                        isBrandsFilterEnabled.value = false
                    }
                }
                .border(
                    width = 1.dp,
                    color = if (isRedeemedFilterEnabled.value) darkGrey else black,
                    shape = RoundedCornerShape(40.dp)
                )
                .background(
                    color = if (isRedeemedFilterEnabled.value) darkGrey else transparent,
                    shape = RoundedCornerShape(40.dp)
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Claimed",
                fontSize = 14.sp,
                modifier = Modifier.padding(
                    start = 12.dp, top = 4.dp, bottom = 5.dp, end = 12.dp
                ),
                color = if (isRedeemedFilterEnabled.value) white else black,
                fontWeight = FontWeight.Medium
            )
        }
    }
}