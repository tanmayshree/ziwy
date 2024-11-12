package com.anonymous.ziwy.Screens.CouponDetailSection.Components

import android.app.Application
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.anonymous.ziwy.GenericComponents.DashedDivider
import com.anonymous.ziwy.MainActivity
import com.anonymous.ziwy.R
import com.anonymous.ziwy.Screens.CouponDetailSection.ViewModel.CouponDetailViewModel
import com.anonymous.ziwy.Screens.CouponDetailSection.ViewModel.CouponDetailViewModelFactory
import com.anonymous.ziwy.Screens.HomeSection.Models.AddCouponRequestModel
import com.anonymous.ziwy.Utilities.Utils.transitionColor
import com.anonymous.ziwy.Utilities.ZColors.black
import com.anonymous.ziwy.Utilities.ZColors.blue
import com.anonymous.ziwy.Utilities.ZColors.grey
import com.anonymous.ziwy.Utilities.ZColors.lightBlueTransparent
import com.anonymous.ziwy.Utilities.ZColors.orange
import com.anonymous.ziwy.Utilities.ZColors.transparent
import com.anonymous.ziwy.Utilities.ZColors.white
import com.anonymous.ziwy.Utilities.ZConstants
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CouponDetailPage(navController: NavHostController, couponId: String?) {

    val context = LocalContext.current
    val application = context.applicationContext as Application

    val viewModel = viewModel<CouponDetailViewModel>(
        key = "CouponDetailViewModel",
        factory = CouponDetailViewModelFactory(application)
    )

    val state = viewModel.state.collectAsState().value

    val coroutineScope = rememberCoroutineScope()

    val modalBottomSheetState = rememberModalBottomSheetState()

    LaunchedEffect(key1 = Unit) {
        viewModel.getUserData(context)
    }

    LaunchedEffect(key1 = state.message) {
        if (state.message.isNullOrEmpty()) return@LaunchedEffect
        Toast.makeText(context as MainActivity, state.message, Toast.LENGTH_LONG).show()
        viewModel.clearMessage()
    }

    Scaffold(
        topBar = {
            Surface(
                color = lightBlueTransparent,
                shadowElevation = 0.3.dp,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        modifier = Modifier
                            .padding(start = 10.dp, end = 10.dp, top = 16.dp, bottom = 16.dp)
                            .clickable {
                                navController.popBackStack()
                            },
                        tint = blue
                    )
                    Text(
                        text = "Coupon Details",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier
                            .padding()
                            .weight(1f),
                        color = blue
                    )
                    // move this logic to the options section if there are multiple options in future
                    if (state.couponsList.firstOrNull { it.couponID == couponId }?.redeemed != true)
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "More",
                            modifier = Modifier
                                .padding(start = 10.dp, end = 10.dp, top = 10.dp, bottom = 10.dp)
                                .clickable {
                                    coroutineScope.launch {
                                        modalBottomSheetState.show()
                                    }
                                },
                            tint = blue
                        )
                }
            }
        },
        containerColor = transparent,
        modifier = Modifier.fillMaxSize()
    ) { it ->

        Column(
            modifier = Modifier
                .padding(it)
                .padding(top = 80.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val coupon = state.couponsList.firstOrNull { it.couponID == couponId }
            if (coupon != null) {
                val cardColor = when (coupon.expiryStatus) {
                    ZConstants.COUPON_HAS_EXPIRED -> grey
                    else -> blue
                }

                val detailsSectionColor = when (coupon.expiryStatus) {
                    ZConstants.COUPON_HAS_EXPIRED -> grey
                    else -> orange
                }

                val textColor = when (coupon.expiryStatus) {
                    ZConstants.COUPON_HAS_EXPIRED -> black
                    else -> white
                }

                Surface(
                    color = cardColor,
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .size(170.dp)
                        .alpha(
                            when (coupon.expiryStatus) {
                                ZConstants.COUPON_HAS_EXPIRED -> 0.7f
                                else -> 1f
                            }
                        ),
                    shadowElevation = 2.dp
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Image(
                            painter = painterResource(R.drawable.coupon_header),
                            contentDescription = "Coupon Image",
                            contentScale = ContentScale.FillWidth,
                            modifier = Modifier
                                .alpha(0.9f)
                                .height(70.dp)
                        )
                        Column(
                            modifier = Modifier
                                .padding(horizontal = 10.dp)
                                .weight(1f),
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            Text(
                                text = coupon.couponBrand ?: "NA",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier
                                    .padding(top = 15.dp, bottom = 10.dp),
                                color = textColor
                            )
                            Text(
                                text = coupon.couponOffer ?: "NA",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier
                                    .padding(),
                                color = textColor
                            )
                        }
                    }
                }
                Surface(
                    color = detailsSectionColor,
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier
                        .alpha(
                            when (coupon.expiryStatus) {
                                ZConstants.COUPON_HAS_EXPIRED -> 0.7f
                                else -> 1f
                            }
                        )
                ) {
                    Column {
                        Text(
                            text = "Offer Details",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.SemiBold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
//                        textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 30.dp, vertical = 16.dp),
                            color = textColor
                        )
                        //horizontal divider with dashed line
                        DashedDivider(textColor)

                        Spacer(modifier = Modifier.height(15.dp))
                        Text(
                            text = "Brand - ${coupon.couponBrand ?: "NA"}",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 30.dp),
                            color = textColor
                        )

                        Text(
                            text = "Offer - ${coupon.couponOffer ?: "NA"}",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 30.dp),
                            color = textColor
                        )

                        //create a string of coupon products separated by comma, couponProduct is an array of strings
                        Text(
                            text = "Products - ${coupon.couponProduct?.joinToString(", ") ?: "NA"}",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 30.dp),
                            color = textColor
                        )

                        Text(
                            text = "Minimum Spend - ${coupon.minSpend ?: "NA"}",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 30.dp),
                            color = textColor
                        )

                        Text(
                            text = "Expiry Date - ${coupon.expiryDate ?: "NA"}",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 30.dp),
                            color = textColor
                        )

                        Text(
                            text = "Redeemed - ${if (coupon.redeemed == true) "Yes" else "No"}",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 30.dp),
                            color = textColor
                        )

                        Spacer(modifier = Modifier.height(15.dp))

                        coupon.couponCode?.let {
                            DashedDivider(textColor)

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        val clipboardManager =
                                            (context as MainActivity).getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                        val clipData = ClipData.newPlainText("text", it)
                                        clipboardManager.setPrimaryClip(clipData)
                                        Toast
                                            .makeText(
                                                context as MainActivity,
                                                "Copied to clipboard",
                                                Toast.LENGTH_LONG
                                            )
                                            .show()
                                    },
                                horizontalArrangement = Arrangement.SpaceAround,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .padding(
                                            start = 10.dp,
                                            end = 10.dp,
                                            top = 8.dp,
                                            bottom = 8.dp
                                        )
                                ) {
                                    // Draw dashed border using Canvas
                                    Canvas(modifier = Modifier.padding(2.dp)) {
                                        val dashEffect = PathEffect.dashPathEffect(
                                            floatArrayOf(10f, 5f),
                                            0f
                                        ) // Dash pattern
                                        drawRoundRect(
                                            color = detailsSectionColor, // White color for the dashed border
                                            size = size.copy(
                                                width = size.width,
                                                height = size.height
                                            ),
                                            style = Stroke(
                                                width = 2f,
                                                pathEffect = dashEffect
                                            ), // Dashed border
                                            topLeft = androidx.compose.ui.geometry.Offset(0f, 0f),
                                            cornerRadius = androidx.compose.ui.geometry.CornerRadius.Zero
                                        )
                                    }

                                    // Text inside the Box
                                    Text(
                                        text = it,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        modifier = Modifier
                                            .background(
                                                textColor,
                                                RoundedCornerShape(10.dp)
                                            ) // Background with rounded corners
                                            .padding(
                                                start = 10.dp,
                                                end = 10.dp,
                                                top = 8.dp,
                                                bottom = 8.dp
                                            ),
                                        color = detailsSectionColor
                                    )
                                }

                                Text(
                                    text = "TAP TO COPY",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Medium,
                                    modifier = Modifier
                                        .padding(
                                            start = 10.dp,
                                            end = 10.dp,
                                            top = 20.dp,
                                            bottom = 20.dp
                                        ),
                                    color = textColor
                                )
                            }
                        }

                    }
                }
            } else {
                Box(
                    modifier = Modifier
                        .size(160.dp)
                        .background(transitionColor(), RoundedCornerShape(10.dp))
                ) {

                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(280.dp)
                        .background(transitionColor(), RoundedCornerShape(20.dp))
                ) {
                    //add 5 lines of text to show loading
                    Column(
                        verticalArrangement = Arrangement.SpaceAround,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 30.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(40.dp)
                                .background(transitionColor(), RoundedCornerShape(10.dp))
                        )
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(0.5f)
                                .height(20.dp)
                                .background(transitionColor(), RoundedCornerShape(10.dp))
                        )
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(0.75f)
                                .height(20.dp)
                                .background(transitionColor(), RoundedCornerShape(10.dp))
                        )
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(20.dp)
                                .background(transitionColor(), RoundedCornerShape(10.dp))
                        )
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(20.dp)
                                .background(transitionColor(), RoundedCornerShape(10.dp))
                        )
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(40.dp)
                                .background(transitionColor(), RoundedCornerShape(10.dp))
                        )
                    }
                }
            }
        }

        if (modalBottomSheetState.isVisible)
            ModalBottomSheet(
                onDismissRequest = {
                    coroutineScope.launch {
                        modalBottomSheetState.hide()
                    }
                },
                sheetState = modalBottomSheetState,
                shape = RoundedCornerShape(topStart = 15.dp, topEnd = 15.dp),
                containerColor = white
            ) {
                Column(
                    modifier = Modifier
                        .padding(start = 16.dp, end = 16.dp, bottom = 20.dp)
                        .fillMaxWidth()
                ) {
                    Text(
                        text = "Mark as Claimed",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                viewModel.updateCoupon(
                                    AddCouponRequestModel(
                                        mobileNumber = state.phoneNumber,
                                        redeemed = true,
                                        countryCode = state.countryCode,
                                        couponID = couponId
                                    )
                                )
                                coroutineScope.launch {
                                    modalBottomSheetState.hide()
                                }
                            }
                            .padding(
                                start = 10.dp,
                                end = 10.dp,
                            ),
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
    }
}