package com.anonymous.ziwy.Utilities.Retrofit

import com.anonymous.ziwy.GenericModels.AppUpdateInfoResponseModel
import com.anonymous.ziwy.Screens.HomeSection.Models.AddCouponRequestModel
import com.anonymous.ziwy.Screens.HomeSection.Models.AddCouponResponseModel
import com.anonymous.ziwy.Screens.HomeSection.Models.CouponsListResponseModel
import com.anonymous.ziwy.Screens.HomeSection.Models.ExtractCouponImageRequestModel
import com.anonymous.ziwy.Screens.HomeSection.Models.ExtractCouponImageResponseModel
import com.anonymous.ziwy.Screens.LoginSection.Models.AddUserInfoRequestModel
import com.anonymous.ziwy.Screens.LoginSection.Models.AddUserInfoResponseModel
import com.anonymous.ziwy.Screens.LoginSection.Models.VerifyUserResponseModel
import com.anonymous.ziwy.Utilities.OpenAi.OpenAiClient
import com.anonymous.ziwy.Utilities.OpenAi.OpenAiRequestModel
import com.anonymous.ziwy.Utilities.OpenAi.OpenAiResponseModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class Repository {

    //getUserData
    fun getUserData(
        mobileNumber: String?,
        countryCode: String?
    ): Flow<Resource<VerifyUserResponseModel>> = flow {
        emit(Resource.Loading())
        val response = ApiResponseHandler.handleApiCall {
            RetrofitClient.apiService.getUserData(mobileNumber, countryCode)
        }
        println("620555 Response: $response ${response.data}")
        emit(response)
    }

    fun addUserInformation(addUserInfoRequestModel: AddUserInfoRequestModel): Flow<Resource<AddUserInfoResponseModel>> =
        flow {
            emit(Resource.Loading())
            val response = ApiResponseHandler.handleApiCall {
                RetrofitClient.apiService.addUserInformation(addUserInfoRequestModel)
            }
            if (response is Resource.Success) {
                if (response.data?.statusCode != null && response.data.statusCode == 201) {
                    emit(Resource.Success(response.data))
                } else {
                    emit(Resource.Error(response.data?.message ?: "Something went wrong!"))
                }
            } else {
                emit(Resource.Error(response.message ?: "Something went wrong!"))
            }
        }

    fun getCouponsList(
        mobileNumber: String?,
        countryCode: String?
    ): Flow<Resource<CouponsListResponseModel>> = flow {
        emit(Resource.Loading())
        val response = ApiResponseHandler.handleApiCall {
            RetrofitClient.apiService.getCouponsList(mobileNumber, countryCode)
        }
        if (response is Resource.Success) {
            if (response.data?.statusCode != null && response.data.statusCode == 200) {
                emit(Resource.Success(response.data))
            } else {
                emit(Resource.Error(response.data?.message ?: "Something went wrong!"))
            }
        } else {
            emit(Resource.Error(response.message ?: "Something went wrong!"))
        }
    }

    fun extractCoupon(
        payload: OpenAiRequestModel
    ): Flow<Resource<OpenAiResponseModel>> = flow {
        emit(Resource.Loading())
        val response = ApiResponseHandler.handleApiCall {
            OpenAiClient.apiService.extractCoupon(payload)
        }
        emit(response)
    }

    //addNewCoupon
    fun addNewCoupon(
        coupon: AddCouponRequestModel
    ): Flow<Resource<AddCouponResponseModel>> = flow {
        emit(Resource.Loading())
        val response = ApiResponseHandler.handleApiCall {
            RetrofitClient.apiService.addNewCoupon(coupon)
        }
        if (response is Resource.Success) {
            if (response.data?.statusCode != null && (response.data.statusCode == 201)) {
                emit(Resource.Success(response.data))
            } else {
                emit(Resource.Error(response.data?.message ?: "Something went wrong!"))
            }
        } else {
            emit(Resource.Error(response.message ?: "Something went wrong!"))
        }
    }

    fun updateCoupon(
        coupon: AddCouponRequestModel
    ): Flow<Resource<AddCouponResponseModel>> = flow {
        emit(Resource.Loading())
        val response = ApiResponseHandler.handleApiCall {
            RetrofitClient.apiService.updateCoupon(coupon)
        }
        if (response is Resource.Success) {
            if (response.data?.body != null) {
                emit(Resource.Success(response.data))
            } else {
                emit(Resource.Error(response.data?.message ?: "Something went wrong!"))
            }
        } else {
            emit(Resource.Error(response.message ?: "Something went wrong!"))
        }
    }

    fun getAppUpdateInfo(): Flow<Resource<AppUpdateInfoResponseModel>> = flow {
        emit(Resource.Loading())
        val response = ApiResponseHandler.handleApiCall {
            RetrofitClient.apiService.getAppUpdateInfo()
        }
        if (response is Resource.Success) {
            if (response.data != null) {
                emit(Resource.Success(response.data))
            } else {
                emit(Resource.Error("Something went wrong!"))
            }
        } else {
            emit(Resource.Error(response.message ?: "Something went wrong!"))
        }
    }

    fun extractCouponImage(
        extractCouponRequestModel: ExtractCouponImageRequestModel
    ): Flow<Resource<ExtractCouponImageResponseModel>> =
        flow {
            emit(Resource.Loading())
            val response = ApiResponseHandler.handleApiCall {
                RetrofitClient.apiService.extractCouponImage(extractCouponRequestModel)
            }
            if (response is Resource.Success) {
                if (response.data != null) {
                    emit(Resource.Success(response.data))
                } else {
                    emit(Resource.Error("Something went wrong!"))
                }
            } else {
                emit(Resource.Error(response.message ?: "Something went wrong!"))
            }
        }
}