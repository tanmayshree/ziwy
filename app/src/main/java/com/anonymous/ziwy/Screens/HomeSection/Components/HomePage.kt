package com.anonymous.ziwy.Screens.HomeSection.Components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.anonymous.ziwy.GenericComponents.ZDialogDropdown
import com.anonymous.ziwy.Screens.HomeSection.ViewModel.MainStore
import com.anonymous.ziwy.Screens.HomeSection.ViewModel.MainViewModel
import com.anonymous.ziwy.Utilities.ZColors.transparent
import com.anonymous.ziwy.Utilities.ZConstants

@Composable
fun HomePage(
    navController: NavHostController,
    viewModel: MainViewModel,
    state: MainStore,
    onCouponClick: (String) -> Unit
) {
    val isExpiringSoonFilterEnabled = remember { mutableStateOf(false) }

    val isProductsFilterEnabled = remember { mutableStateOf(false) }

    val isBrandsFilterEnabled = remember { mutableStateOf(false) }

    val isProductFilterDropdownVisible = remember { mutableStateOf(false) }

    val isBrandFilterDropdownVisible = remember { mutableStateOf(false) }

    val isActiveFilterEnabled = remember { mutableStateOf(false) }

    val isRedeemedFilterEnabled = remember { mutableStateOf(false) }

    val selectedProductFilter = remember { mutableStateOf<String?>(null) }

    val selectedBrandFilter = remember { mutableStateOf<String?>(null) }

    val filteredList = state.couponsList
        .asSequence()
        .filter { if (isRedeemedFilterEnabled.value) (it.redeemed == true && it.expiryStatus != ZConstants.COUPON_HAS_EXPIRED) else (it.redeemed != true) }
        .filter { if (isActiveFilterEnabled.value) (it.expiryStatus != ZConstants.COUPON_HAS_EXPIRED) else true }
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
        .toList()

    val productList = listOf("All Products") + state.couponsList
        .mapNotNull { it.couponProduct }.flatten()
        .distinct()

    val brandList = listOf("All Brands") + state.couponsList
        .mapNotNull { it.couponBrand }.distinct()

    LaunchedEffect(Unit) {
        viewModel.getCarouselImages()
    }

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
                    selectedProductFilter = selectedProductFilter,
                    isBrandsFilterEnabled = isBrandsFilterEnabled,
                    onBrandFilterClick = {
                        isBrandFilterDropdownVisible.value = !isBrandFilterDropdownVisible.value
                    },
                    selectedBrandFilter = selectedBrandFilter,
                    isActiveFilterEnabled = isActiveFilterEnabled,
                    isRedeemedFilterEnabled = isRedeemedFilterEnabled
                )
                CouponsContainer(
                    viewModel = viewModel,
                    state = state,
                    filteredList = filteredList,
                    onCouponClick = onCouponClick,
                )
            }
            //Animated Strip
            /*AnimatedVisibility(
                visible = !state.isEmailSynced,
                enter = fadeIn() + slideInVertically { +it / 2 },
                exit = fadeOut() + slideOutVertically { +it / 2 }
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    SignInToGoogleStrip(state)
                }
            }*/

        }
    }
}