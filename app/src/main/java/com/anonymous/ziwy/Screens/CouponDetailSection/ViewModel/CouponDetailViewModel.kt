package com.anonymous.ziwy.Screens.CouponDetailSection.ViewModel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.anonymous.ziwy.GenericModels.LoadingScreenState
import com.anonymous.ziwy.Screens.HomeSection.Models.AddCouponRequestModel
import com.anonymous.ziwy.Utilities.Retrofit.Repository
import com.anonymous.ziwy.Utilities.Retrofit.Resource
import com.anonymous.ziwy.Utilities.Utils
import com.anonymous.ziwy.Utilities.ZConstants
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CouponDetailViewModel(
    private val application: Application
) : AndroidViewModel(application) {

    private val _state = MutableStateFlow(CouponDetailStore())
    val state: StateFlow<CouponDetailStore> = _state

    private val repository = Repository()

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
