package com.anonymous.ziwy.Screens.HomeSection.Components

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.anonymous.ziwy.GenericComponents.ZDialogDropdown
import com.anonymous.ziwy.GenericModels.LoadingScreenState
import com.anonymous.ziwy.MainActivity
import com.anonymous.ziwy.R
import com.anonymous.ziwy.Screens.HomeSection.Models.AddCouponRequestModel
import com.anonymous.ziwy.Screens.HomeSection.Models.Coupon
import com.anonymous.ziwy.Screens.HomeSection.ViewModel.MainStore
import com.anonymous.ziwy.Screens.HomeSection.ViewModel.MainViewModel
import com.anonymous.ziwy.Utilities.ZColors.black
import com.anonymous.ziwy.Utilities.ZColors.grey
import com.anonymous.ziwy.Utilities.ZColors.lightBlue
import com.anonymous.ziwy.Utilities.ZColors.lightGrey
import com.anonymous.ziwy.Utilities.ZColors.orange
import com.anonymous.ziwy.Utilities.ZColors.transparent
import com.anonymous.ziwy.Utilities.ZColors.white
import com.anonymous.ziwy.Utilities.ZConstants

@Composable
fun HomePage(
    navController: NavHostController, viewModel: MainViewModel, state: MainStore, context: Context
) {
    val isExpiringSoonFilterEnabled = remember { mutableStateOf(false) }
    val isProductsFilterEnabled = remember { mutableStateOf(false) }
    val isBrandsFilterEnabled = remember { mutableStateOf(false) }

    val isProductFilterDropdownVisible = remember { mutableStateOf(false) }
    val isBrandFilterDropdownVisible = remember { mutableStateOf(false) }

    val productList =
        listOf("All Products") + state.couponsList.filter { it.redeemed != true }
            .mapNotNull { it.couponProduct }.flatten()
            .distinct()
    val brandList =
        listOf("All Brands") + state.couponsList.filter { it.redeemed != true }
            .mapNotNull { it.couponBrand }.distinct()

    val selectedProductFilter = remember { mutableStateOf<String?>(null) }
    val selectedBrandFilter = remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = { WelcomeHeader(state) },
        containerColor = transparent
    ) { it ->
        Box(modifier = Modifier.fillMaxSize()) {
            ZDialogDropdown(isProductFilterDropdownVisible, productList) { item, index ->
                if (index == 0) {
                    selectedProductFilter.value = null
                    isProductsFilterEnabled.value = false
                } else {
                    selectedProductFilter.value = item
                    isProductsFilterEnabled.value = true
                }
            }
            ZDialogDropdown(isBrandFilterDropdownVisible, brandList) { item, index ->
                if (index == 0) {
                    selectedBrandFilter.value = null
                    isBrandsFilterEnabled.value = false
                } else {
                    selectedBrandFilter.value = item
                    isBrandsFilterEnabled.value = true
                }
            }
            Column(
                modifier = Modifier
                    .padding(it)
                    .fillMaxWidth(), verticalArrangement = Arrangement.Center
            ) {
                FilterChips(
                    isExpiringSoonFilterEnabled = isExpiringSoonFilterEnabled,
                    isProductsFilterEnabled = isProductsFilterEnabled,
                    onProductFilterClick = {
                        isProductFilterDropdownVisible.value = !isProductFilterDropdownVisible.value
                    },
                    isBrandsFilterEnabled = isBrandsFilterEnabled,
                    onBrandFilterClick = {
                        isBrandFilterDropdownVisible.value = !isBrandFilterDropdownVisible.value
                    }
                )
                CouponsContainer(
                    viewModel = viewModel,
                    state = state,
                    context = context,
                    isExpiringSoonFilterEnabled = isExpiringSoonFilterEnabled,
                    selectedProductFilter = selectedProductFilter,
                    selectedBrandFilter = selectedBrandFilter
                )
            }
        }
    }
}

@Composable
private fun CouponsContainer(
    viewModel: MainViewModel,
    state: MainStore,
    context: Context,
    isExpiringSoonFilterEnabled: MutableState<Boolean>,
    selectedProductFilter: MutableState<String?>,
    selectedBrandFilter: MutableState<String?>,
) {
    LazyVerticalGrid(
        contentPadding = PaddingValues(horizontal = 16.dp),
        columns = GridCells.Fixed(2),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        if (state.imageUri != null && state.imageUri != Uri.EMPTY) {
            item {
                val infiniteTransition = rememberInfiniteTransition(label = "")

                val color by infiniteTransition.animateColor(
                    initialValue = lightGrey,
                    targetValue = grey,
                    animationSpec = infiniteRepeatable(
                        animation = tween(1000, easing = LinearEasing),
                        repeatMode = RepeatMode.Reverse
                    ), label = ""
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(color, RoundedCornerShape(10.dp))
                ) {
                    Image(
                        painter = rememberAsyncImagePainter(model = state.imageUri),
                        contentDescription = null,
                        modifier = Modifier
                            .alpha(.4f)
                            .fillMaxWidth()
//                            .width(150.dp)
                            .height(154.dp)
                            .padding(1.dp),
                        contentScale = ContentScale.FillBounds
                    )
                    //add shimmer

                }
            }
        }

        if (state.loadingScreenState == LoadingScreenState(true, ZConstants.FETCH_COUPONS)) {
            items(8) {
                val infiniteTransition = rememberInfiniteTransition(label = "")
                val color by infiniteTransition.animateColor(
                    initialValue = lightGrey,
                    targetValue = grey,
                    animationSpec = infiniteRepeatable(
                        animation = tween(1000, easing = LinearEasing),
                        repeatMode = RepeatMode.Reverse
                    ), label = ""
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
//                        .width(150.dp)
                        .height(154.dp)
                        .background(color, RoundedCornerShape(10.dp))
                ) {

                }
            }
        } else {
            val filteredList = state.couponsList
                .filter { it.redeemed != true }
                .filter {
                    it.expiryStatus == ZConstants.COUPON_IS_EXPIRING_SOON || !isExpiringSoonFilterEnabled.value
                }
                .filter {
                    selectedProductFilter.value == null || it.couponProduct?.contains(
                        selectedProductFilter.value
                    ) == true
                }
                .filter {
                    selectedBrandFilter.value == null || it.couponBrand == selectedBrandFilter.value
                }
            items(filteredList) { item ->
                val isCardFlipped = remember { mutableStateOf(false) }
                AnimatedContent(
                    targetState = isCardFlipped.value,
                    transitionSpec = {
                        if (targetState) {
                            // Flip to back: front -> back
                            (fadeIn() + slideInHorizontally { it / 2 }).togetherWith(fadeOut() + slideOutHorizontally { -it / 2 })
                        } else {
                            // Flip to front: back -> front
                            (fadeIn() + slideInHorizontally { -it / 2 }).togetherWith(fadeOut() + slideOutHorizontally { it / 2 })
                        }
                    }, label = ""
                ) { flipped ->
                    if (flipped) {
                        CouponItemCardBack(item, isCardFlipped, context, viewModel, state)
                    } else {
                        CouponItemCardFront(item, isCardFlipped, viewModel)
                    }
                }
            }
        }
    }
}

@Composable
private fun CouponItemCardFront(
    item: Coupon,
    isCardFlipped: MutableState<Boolean>,
    viewModel: MainViewModel
) {

    Surface(
        shape = RoundedCornerShape(10.dp),
        color =
//        if (item.redeemed == true) lightGrey else
        when (item.expiryStatus) {
            ZConstants.COUPON_HAS_EXPIRED -> lightGrey
            ZConstants.COUPON_IS_EXPIRING_SOON -> lightBlue
            else -> orange
        },
        modifier = Modifier
            .alpha(if (item.expiryStatus == ZConstants.COUPON_HAS_EXPIRED) 0.5f else 1f)
            .clickable {
                isCardFlipped.value =
                    (item.expiryStatus != ZConstants.COUPON_HAS_EXPIRED)
            }
    ) {
        Box(
            modifier = Modifier
//                .width(150.dp)
                .height(154.dp)
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(10.dp)
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
                        )
                )
                Text(
                    text = item.couponOffer ?: "OFFER",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Left,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            start = 10.dp,
                            top = 20.dp,
                            end = 10.dp,
                        )
                )
                Text(
                    text = "Expiry Date: ${item.expiryDate ?: "NA"}",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Normal,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Left,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            start = 10.dp,
                            end = 10.dp,
                        )
                )
                Text(text = "Min Spend: ${item.minSpend?.let { "₹$it" } ?: "NA"}",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Normal,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Left,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            start = 10.dp,
                            end = 10.dp,
                        )
                )
            }
        }
    }
}

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
        ZConstants.COUPON_IS_EXPIRING_SOON -> lightBlue
        else -> orange
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

@Composable
private fun FilterChips(
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
                    color = if (isExpiringSoonFilterEnabled.value) lightBlue else black,
                    shape = RoundedCornerShape(40.dp)
                )
                .background(
                    color = if (isExpiringSoonFilterEnabled.value) lightBlue else transparent,
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
                    color = if (isProductsFilterEnabled.value) orange else black,
                    shape = RoundedCornerShape(40.dp)
                )
                .background(
                    color = if (isProductsFilterEnabled.value) orange else transparent,
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
                    color = if (isBrandsFilterEnabled.value) orange else black,
                    shape = RoundedCornerShape(40.dp)
                )
                .background(
                    color = if (isBrandsFilterEnabled.value) orange else transparent,
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

@Composable
fun WelcomeHeader(state: MainStore) {
    Text(
        text = "Welcome, ${state.username ?: "User"}!", fontSize = 20.sp,
        modifier = Modifier
            .padding(horizontal = 14.dp),
        fontWeight = FontWeight.SemiBold,
    )
}