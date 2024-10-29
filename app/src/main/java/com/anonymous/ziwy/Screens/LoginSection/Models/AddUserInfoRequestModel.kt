package com.anonymous.ziwy.Screens.LoginSection.Models

import kotlinx.serialization.Serializable

@Serializable
data class AddUserInfoRequestModel(
    val countryCode: String? = null,
    val mobileNumber: String? = null,
    val email: String? = null,
    val userName: String? = null,
    val gender: String? = null,
    val ageGroup: String? = null,
    val notificationId: String? = null
)
