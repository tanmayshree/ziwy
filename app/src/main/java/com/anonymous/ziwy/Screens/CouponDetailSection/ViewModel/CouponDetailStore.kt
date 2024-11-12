package com.anonymous.ziwy.Screens.CouponDetailSection.ViewModel

import com.anonymous.ziwy.GenericModels.LoadingScreenState
import com.anonymous.ziwy.Screens.HomeSection.Models.Coupon

data class CouponDetailStore(
    val isLoading: Boolean? = null,
    val loadingScreenState: LoadingScreenState? = null,
    val message: String? = null,
    val couponsList: List<Coupon> = emptyList(),

    val username: String? = null,
    val countryCode: String? = null,
    val phoneNumber: String? = null,
)
