package com.anonymous.ziwy.Screens.HomeSection.ViewModel

import android.app.Application
import android.content.Context
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.anonymous.ziwy.GenericModels.LoadingScreenState
import com.anonymous.ziwy.Screens.HomeSection.Models.AddCouponRequestModel
import com.anonymous.ziwy.Screens.HomeSection.Models.ExtractCouponImageRequestModel
import com.anonymous.ziwy.Screens.HomeSection.Models.ExtractCouponImageResponseModel
import com.anonymous.ziwy.Utilities.Retrofit.Repository
import com.anonymous.ziwy.Utilities.Retrofit.Resource
import com.anonymous.ziwy.Utilities.Utils
import com.anonymous.ziwy.Utilities.ZConstants
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainViewModel(
    private val application: Application
) : AndroidViewModel(application) {

    private val _state = MutableStateFlow(MainStore())
    val state: StateFlow<MainStore> = _state

    private val repository = Repository()

    fun setImageUri(uri: Uri) {
        _state.value = _state.value.copy(
            imageUri = uri
        )
    }

    fun clearMessage() {
        _state.value = _state.value.copy(
            message = null
        )
    }

    fun getUserData(context: Context) {
        viewModelScope.launch {
            delay(1000L)
            val userDetails = Utils.getUserDetailsFromPreferences(context)
            _state.value = _state.value.copy(
                username = userDetails.username,
                countryCode = userDetails.countryCode,
                phoneNumber = userDetails.phoneNumber,
                joiningDate = userDetails.joiningDate?.let { Utils.formatDateTime(it) }
            )
            println("620555 MainViewModel getUserData Success $userDetails")
            fetchCouponsList(userDetails.phoneNumber, userDetails.countryCode)
        }
    }

    fun fetchCouponsList(mobileNumber: String?, countryCode: String?) {
        viewModelScope.launch {
            repository.getCouponsList(mobileNumber, countryCode).collect { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        _state.value = _state.value.copy(
                            isLoading = true,
                            loadingScreenState = LoadingScreenState(
                                isLoading = true,
                                screen = ZConstants.FETCH_COUPONS
                            )
                        )
                        println("620555 MainViewModel fetchCouponsList Loading...")
                    }

                    is Resource.Success -> {

                        val couponsList = resource.data?.body?.map {
                            it.copy(
                                expiryStatus = it.expiryDate?.let { date ->
                                    Utils.checkDateDifference(date)?.let { daysDifference ->
                                        when {
                                            (daysDifference < 0L) -> ZConstants.COUPON_HAS_EXPIRED
                                            (daysDifference <= 5L) -> ZConstants.COUPON_IS_EXPIRING_SOON
                                            else -> ZConstants.COUPON_IS_VALID
                                        }
                                    } ?: ZConstants.COUPON_IS_VALID
                                } ?: ZConstants.COUPON_IS_VALID
                            )
                        } ?: emptyList()

                        _state.value = _state.value.copy(
                            isLoading = false,
                            loadingScreenState = LoadingScreenState(
                                isLoading = false,
                                screen = ZConstants.FETCH_COUPONS
                            ),
                            couponsList = couponsList,
//                            message = "Coupons fetched successfully"
                        )
                        println("620555 MainViewModel fetchCouponsList Success ${resource.data}")
                    }

                    is Resource.Error -> {
                        _state.value = _state.value.copy(
                            isLoading = false,
                            loadingScreenState = LoadingScreenState(
                                isLoading = false,
                                screen = ZConstants.FETCH_COUPONS
                            ),
                            couponsList = emptyList(),
//                            message = resource.message
                        )
                        println("620555 MainViewModel fetchCouponsList Error ${resource.message}")
                    }
                }
            }
        }

    }

    /*fun extractCoupon(context: Context, imageUri: Uri, payload: OpenAiRequestModel) {
        viewModelScope.launch {
            repository.extractCoupon(payload).collect { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        _state.value = _state.value.copy(
                            isLoading = true,
                            loadingScreenState = LoadingScreenState(
                                isLoading = true,
                                screen = ZConstants.EXTRACT_COUPON,
                            ),
                            imageUri = imageUri
                        )
                        println("620555 MainViewModel extractCoupon Loading...")
                    }

                    is Resource.Success -> {
                        _state.value = _state.value.copy(
                            isLoading = false,
                            loadingScreenState = LoadingScreenState(
                                isLoading = false,
                                screen = ZConstants.EXTRACT_COUPON
                            ),
//                            message = "Coupon extracted successfully"
                        )
                        val couponData = Utils.parseOpenAiResponseForCouponData(
                            resource.data?.choices?.getOrNull(0)?.message?.content ?: ""
                        )
                        println("620555 MainViewModel extractCoupon Success ${resource.data}")
                        if (couponData.coupon_code.isNullOrEmpty() or couponData.brand.isNullOrEmpty() or couponData.offer.isNullOrEmpty()) {
                            _state.value = _state.value.copy(
                                message = "Please check if the coupon code and brand names are clearly visible.",
                                imageUri = null
                            )
                        } else {
                            val userDetails = Utils.getUserDetailsFromPreferences(context)
                            couponData.let {
                                addNewCoupon(
                                    AddCouponRequestModel(
                                        mobileNumber = userDetails.phoneNumber,
                                        countryCode = userDetails.countryCode,
                                        couponCode = it.coupon_code,
                                        userName = userDetails.username,
                                        couponBrand = it.brand,
                                        couponProduct = it.product,
                                        couponOffer = it.offer,
                                        expiryDate = it.expiry_date,
                                        redeemed = false,
                                        minSpend = it.spend?.toIntOrNull(),
                                        couponSource = null
                                    )
                                )
                            }
                        }
                    }

                    is Resource.Error -> {
                        _state.value = _state.value.copy(
                            isLoading = false,
                            loadingScreenState = LoadingScreenState(
                                isLoading = false,
                                screen = ZConstants.EXTRACT_COUPON
                            ),
                            imageUri = null,
                            message = resource.message
                        )
                        println("620555 MainViewModel extractCoupon Error ${resource.message}")
                    }
                }
            }
        }
    }*/

    fun extractCouponImage(
        context: Context,
        imageUri: Uri,
        payload: ExtractCouponImageRequestModel
    ) {
        viewModelScope.launch {
            repository.extractCouponImage(payload).collect { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        _state.value = _state.value.copy(
                            isLoading = true,
                            loadingScreenState = LoadingScreenState(
                                isLoading = true,
                                screen = ZConstants.EXTRACT_COUPON,
                            ),
                            imageUri = imageUri
                        )
                        println("620555 MainViewModel extractCoupon Loading...")
                    }

                    is Resource.Success -> {
                        _state.value = _state.value.copy(
                            isLoading = false,
                            loadingScreenState = LoadingScreenState(
                                isLoading = false,
                                screen = ZConstants.EXTRACT_COUPON
                            ),
//                            message = "Coupon extracted successfully"
                        )
                        val couponData = resource.data ?: ExtractCouponImageResponseModel()
                        println("620555 MainViewModel extractCoupon Success ${resource.data}")
                        if (couponData.couponBrand.isNullOrEmpty() or couponData.couponOffer.isNullOrEmpty()) {
                            _state.value = _state.value.copy(
                                message = "Please check if the brand name and offers are clearly visible.",
                                imageUri = null
                            )
                        } else {
                            val userDetails = Utils.getUserDetailsFromPreferences(context)
                            couponData.let {
                                addNewCoupon(
                                    AddCouponRequestModel(
                                        mobileNumber = userDetails.phoneNumber,
                                        countryCode = userDetails.countryCode,
                                        couponCode = it.couponCode.takeIf { !it.isNullOrEmpty() } ?: "",
                                        userName = userDetails.username,
                                        couponBrand = it.couponBrand,
                                        couponProduct = it.couponProduct,
                                        couponOffer = it.couponOffer,
                                        expiryDate = it.expiryDate,
                                        redeemed = false,
                                        minSpend = it.minSpend?.toIntOrNull(),
                                        couponSource = null
                                    )
                                )
                                _state.value = _state.value.copy(
                                    message = "Coupon code not found in the image.",
                                )
                            }
                        }
                    }

                    is Resource.Error -> {
                        _state.value = _state.value.copy(
                            isLoading = false,
                            loadingScreenState = LoadingScreenState(
                                isLoading = false,
                                screen = ZConstants.EXTRACT_COUPON
                            ),
                            imageUri = null,
                            message = resource.message
                        )
                        println("620555 MainViewModel extractCoupon Error ${resource.message}")
                    }
                }
            }
        }
    }

    private fun addNewCoupon(coupon: AddCouponRequestModel) {
        viewModelScope.launch {
            repository.addNewCoupon(coupon).collect { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        _state.value = _state.value.copy(
                            isLoading = true,
                            loadingScreenState = LoadingScreenState(
                                isLoading = true,
                                screen = ZConstants.ADD_COUPON
                            )
                        )
                        println("620555 MainViewModel addNewCoupon Loading...")
                    }

                    is Resource.Success -> {
                        _state.value = _state.value.copy(
                            isLoading = false,
                            loadingScreenState = LoadingScreenState(
                                isLoading = false,
                                screen = ZConstants.ADD_COUPON
                            ),
                            imageUri = null,
                            message = resource.data?.message ?: "Coupon added successfully"
                        )
                        fetchCouponsList(_state.value.phoneNumber, _state.value.countryCode)
                        println("620555 MainViewModel addNewCoupon Success ${resource.data}")
                    }

                    is Resource.Error -> {
                        _state.value = _state.value.copy(
                            isLoading = false,
                            loadingScreenState = LoadingScreenState(
                                isLoading = false,
                                screen = ZConstants.ADD_COUPON
                            ),
                            imageUri = null,
                            message = resource.message
                        )
                        println("620555 MainViewModel addNewCoupon Error ${resource.message}")
                    }
                }
            }
        }
    }

    fun updateCoupon(coupon: AddCouponRequestModel) {
        viewModelScope.launch {
            repository.updateCoupon(coupon).collect { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        _state.value = _state.value.copy(
                            isLoading = true,
                            loadingScreenState = LoadingScreenState(
                                isLoading = true,
                                screen = ZConstants.UPDATE_COUPON
                            )
                        )
                        println("620555 MainViewModel updateCoupon Loading...")
                    }

                    is Resource.Success -> {
                        val couponList = _state.value.couponsList
                        val updatedList =
                            couponList.map { if (it.couponID == coupon.couponID) it.copy(redeemed = true) else it }

                        _state.value = _state.value.copy(
                            couponsList = updatedList,
                            isLoading = false,
                            loadingScreenState = LoadingScreenState(
                                isLoading = false,
                                screen = ZConstants.UPDATE_COUPON
                            ),
                            message = resource.data?.message ?: "Coupon updated successfully"
                        )
                        println("620555 MainViewModel updateCoupon Success ${resource.data}")
                    }

                    is Resource.Error -> {
                        _state.value = _state.value.copy(
                            isLoading = false,
                            loadingScreenState = LoadingScreenState(
                                isLoading = false,
                                screen = ZConstants.UPDATE_COUPON
                            ),
                            message = resource.message
                        )
                        println("620555 MainViewModel updateCoupon Error ${resource.message}")
                    }
                }
            }
        }
    }
}
