package com.anonymous.ziwy.Screens.LoginSection.Models

import kotlinx.serialization.Serializable

@Serializable
data class VerifyUserResponseModel(
    val statusCode: Int? = null,
    val body: VerifyUser? = null,
    val message: String? = null
)

@Serializable
data class VerifyUser(
    val countryCode: String? = null,
    val mobileNumber: String? = null,
    val email: String? = null,
    val userName: String? = null,
    val gender: String? = null,
    val ageGroup: String? = null,
    val notificationId: String? = null,
    val creationTime: String? = null,
    val emailSync: Boolean? = null,
//    val primaryKey: String? = null
)