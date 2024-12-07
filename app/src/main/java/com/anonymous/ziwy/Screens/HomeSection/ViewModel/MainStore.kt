package com.anonymous.ziwy.Screens.HomeSection.ViewModel

import android.net.Uri
import com.anonymous.ziwy.GenericModels.LoadingScreenState
import com.anonymous.ziwy.Screens.HomeSection.Models.Carousel
import com.anonymous.ziwy.Screens.HomeSection.Models.Coupon

data class MainStore(
    val isLoading: Boolean? = null,
    val loadingScreenState: LoadingScreenState? = null,
    val message: String? = null,
    val imageUri: Uri? = null,
    val couponsList: List<Coupon> = emptyList(),

    val username: String? = null,
    val countryCode: String? = null,
    val phoneNumber: String? = null,
    val joiningDate: String? = null,
    val isEmailSynced: Boolean = true,

    val carouselImagesList: ArrayList<Carousel> = arrayListOf(),
)
