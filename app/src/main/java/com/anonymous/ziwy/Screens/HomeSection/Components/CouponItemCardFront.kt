package com.anonymous.ziwy.Screens.HomeSection.Components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.anonymous.ziwy.R
import com.anonymous.ziwy.Screens.HomeSection.Models.Coupon
import com.anonymous.ziwy.Utilities.ZColors
import com.anonymous.ziwy.Utilities.ZColors.black
import com.anonymous.ziwy.Utilities.ZColors.blue
import com.anonymous.ziwy.Utilities.ZColors.grey
import com.anonymous.ziwy.Utilities.ZColors.white
import com.anonymous.ziwy.Utilities.ZConstants

@Composable
fun CouponItemCardFront(
    item: Coupon,
    onCouponClick: (String) -> Unit,
) {

    val containerColor = if (item.redeemed == true) grey else when (item.expiryStatus) {
        ZConstants.COUPON_HAS_EXPIRED -> grey
        ZConstants.COUPON_IS_EXPIRING_SOON -> blue
        else -> ZColors.orange
    }

    val textColor = if (item.redeemed == true) black else when (item.expiryStatus) {
        ZConstants.COUPON_HAS_EXPIRED -> black
        ZConstants.COUPON_IS_EXPIRING_SOON -> white
        else -> white
    }

    val viewDetailsColor = if (item.redeemed == true) black else when (item.expiryStatus) {
        ZConstants.COUPON_HAS_EXPIRED -> black
        else -> containerColor
    }

    Surface(
        shape = RoundedCornerShape(10.dp),
        border = BorderStroke(2.dp, containerColor),
        color = containerColor,
        modifier = Modifier
            .alpha(if (item.expiryStatus == ZConstants.COUPON_HAS_EXPIRED) 1f else 1f)
            .clickable {
//                isCardFlipped.value =
//                    (item.expiryStatus != ZConstants.COUPON_HAS_EXPIRED)
                if (item.expiryStatus != ZConstants.COUPON_HAS_EXPIRED)
                    item.couponID?.let { onCouponClick.invoke(it) }
            }
    ) {
        Box(
            modifier = Modifier
//                .width(150.dp)
                .height(180.dp)
                .fillMaxWidth()
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxSize()
            ) {
                Column(
                    modifier = Modifier.padding(10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = item.couponBrand ?: "BRAND",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Medium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                start = 10.dp, end = 10.dp, top = 10.dp, bottom = 10.dp
                            ),
                        color = textColor
                    )
                    Text(
                        text = item.couponOffer ?: "OFFER",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = textColor
                    )
                    Text(
                        text = "Expiry Date - ${item.expiryDate ?: "NA"}",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Medium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = textColor,
                    )
                    Text(
                        text = "Min Spend - ${item.minSpend?.let { "₹$it" } ?: "NA"}",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Medium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = textColor
                    )

                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(white),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (item.expiryStatus != ZConstants.COUPON_HAS_EXPIRED)
                            "View Details" else "EXPIRED",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        textAlign = TextAlign.Left,
                        modifier = Modifier
//                        .fillMaxWidth()
                            .padding(vertical = 10.dp, horizontal = 5.dp),
                        color = viewDetailsColor
                    )
                    if (item.expiryStatus != ZConstants.COUPON_HAS_EXPIRED)
                        Image(
                            painterResource(id = R.drawable.fast_forward),
                            contentDescription = "Home",
                            modifier = Modifier.size(12.dp),
                            colorFilter = ColorFilter.tint(viewDetailsColor)
                        )
                }
            }
        }
    }
}