package com.anonymous.ziwy.Screens.HomeSection.Components

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.anonymous.ziwy.MainActivity
import com.anonymous.ziwy.Screens.HomeSection.Models.AddCouponRequestModel
import com.anonymous.ziwy.Screens.HomeSection.Models.Coupon
import com.anonymous.ziwy.Screens.HomeSection.ViewModel.MainStore
import com.anonymous.ziwy.Screens.HomeSection.ViewModel.MainViewModel
import com.anonymous.ziwy.Utilities.ZColors
import com.anonymous.ziwy.Utilities.ZColors.blue
import com.anonymous.ziwy.Utilities.ZColors.lightGrey
import com.anonymous.ziwy.Utilities.ZColors.transparent
import com.anonymous.ziwy.Utilities.ZColors.white
import com.anonymous.ziwy.Utilities.ZConstants

@Composable
fun CouponItemCardBack(
    item: Coupon,
    isCardFlipped: MutableState<Boolean>,
    context: Context,
    viewModel: MainViewModel,
    state: MainStore,
) {
    val cardColor = when (item.expiryStatus) {
        ZConstants.COUPON_HAS_EXPIRED -> lightGrey
        ZConstants.COUPON_IS_EXPIRING_SOON -> blue
        else -> ZColors.orange
    }
    Column(
        modifier = Modifier
//            .width(150.dp)
            .height(154.dp)
            .fillMaxWidth()
            .background(lightGrey, RoundedCornerShape(10.dp)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // cross icon on top right and two buttons Copy Code and Redeem option
        Box(
            modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd
        ) {
            Surface(
                modifier = Modifier.clickable { isCardFlipped.value = false },
                color = transparent
            ) {
                Icon(
                    Icons.Filled.Close,
                    contentDescription = "Home",
                    modifier = Modifier
                        .size(40.dp)
                        .padding(10.dp),
                )
            }
        }

        Surface(
            color = cardColor,
            modifier = Modifier
                .shadow(10.dp, RoundedCornerShape(40.dp))
                .clip(RoundedCornerShape(40.dp))
                .clickable {
                    val clipboardManager =
                        (context as MainActivity).getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                    val clipData = ClipData.newPlainText("text", item.couponCode)
                    clipboardManager.setPrimaryClip(clipData)
                    Toast
                        .makeText(
                            context as MainActivity, "Copied to clipboard", Toast.LENGTH_LONG
                        )
                        .show()
                },
        ) {
            Text(
                text = "Copy Code", fontSize = 15.sp, modifier = Modifier.padding(
                    start = 10.dp, top = 4.dp, bottom = 5.dp, end = 10.dp
                ), color = white, fontWeight = FontWeight.Medium
            )
        }

        Spacer(modifier = Modifier.size(12.dp))
        Surface(
            color = cardColor,
            modifier = Modifier
                .shadow(10.dp, RoundedCornerShape(40.dp))
                .clip(RoundedCornerShape(40.dp))
                .clickable {
                    viewModel.updateCoupon(
                        AddCouponRequestModel(
                            mobileNumber = state.phoneNumber,
                            redeemed = true,
                            countryCode = state.countryCode,
                            couponID = item.couponID
                        )
                    )
                },
        ) {
            Text(
                text = "Redeemed", fontSize = 15.sp, modifier = Modifier.padding(
                    start = 10.dp, top = 4.dp, bottom = 5.dp, end = 10.dp
                ), color = white, fontWeight = FontWeight.Medium
            )
        }

    }
}