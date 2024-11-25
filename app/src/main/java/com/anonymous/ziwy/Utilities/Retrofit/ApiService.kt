package com.anonymous.ziwy.Utilities.Retrofit

import com.anonymous.ziwy.GenericModels.AppUpdateInfoResponseModel
import com.anonymous.ziwy.Screens.CardsSection.Models.CardsListResponseModel
import com.anonymous.ziwy.Screens.CardsSection.Models.UpdateUserCardRequestModel
import com.anonymous.ziwy.Screens.CardsSection.Models.UpdateUserCardResponseModel
import com.anonymous.ziwy.Screens.CardsSection.Models.UserCardsListResponseModel
import com.anonymous.ziwy.Screens.HomeSection.Models.AddCouponRequestModel
import com.anonymous.ziwy.Screens.HomeSection.Models.AddCouponResponseModel
import com.anonymous.ziwy.Screens.HomeSection.Models.CouponsListResponseModel
import com.anonymous.ziwy.Screens.HomeSection.Models.ExtractCouponImageRequestModel
import com.anonymous.ziwy.Screens.HomeSection.Models.ExtractCouponImageResponseModel
import com.anonymous.ziwy.Screens.LoginSection.Models.AddUserInfoRequestModel
import com.anonymous.ziwy.Screens.LoginSection.Models.AddUserInfoResponseModel
import com.anonymous.ziwy.Screens.LoginSection.Models.VerifyUserResponseModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

interface ApiService {
    //    @GET("posts")
//    suspend fun getPosts(): Response<List<Post>>
    @GET("user-data")
    suspend fun getUserData(
        @Query("mobileNumber") mobileNumber: String?,
        @Query("countryCode") countryCode: String?
    ): Response<VerifyUserResponseModel>

    @POST("user-data")
    suspend fun addUserInformation(
        @Body addUserInfoRequestModel: AddUserInfoRequestModel
    ): Response<AddUserInfoResponseModel>

    @GET("user-coupon")
    suspend fun getCouponsList(
        @Query("mobileNumber") mobileNumber: String?,
        @Query("countryCode") countryCode: String?
    ): Response<CouponsListResponseModel>

    //add coupon
    @POST("user-coupon")
    suspend fun addNewCoupon(
        @Body coupon: AddCouponRequestModel
    ): Response<AddCouponResponseModel>

    //update coupon
    @PUT("user-coupon")
    suspend fun updateCoupon(
        @Body coupon: AddCouponRequestModel
    ): Response<AddCouponResponseModel>

    //get app update info
    @GET("getAppUpdateInfo")
    suspend fun getAppUpdateInfo(): Response<AppUpdateInfoResponseModel>

    @POST("coupon-extraction-image")
    suspend fun extractCouponImage(
        @Body extractCouponRequestModel: ExtractCouponImageRequestModel
    ): Response<ExtractCouponImageResponseModel>

    @GET("all-credit-cards")
    suspend fun getCardsList(
        @Query("mobileNumber") mobileNumber: String?,
        @Query("countryCode") countryCode: String?
    ): Response<CardsListResponseModel>

    @GET("user-credit-card")
    suspend fun getUserCardList(
        @Query("mobileNumber") mobileNumber: String?,
        @Query("countryCode") countryCode: String?
    ): Response<UserCardsListResponseModel>

    @PUT("user-credit-card")
    suspend fun updateUserCardList(
        @Body userCardsList: UpdateUserCardRequestModel
    ): Response<UpdateUserCardResponseModel>
}