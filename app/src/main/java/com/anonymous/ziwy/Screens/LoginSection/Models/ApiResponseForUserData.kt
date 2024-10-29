package com.anonymous.ziwy.Screens.LoginSection.Models

import kotlinx.serialization.Serializable

@Serializable
sealed class ApiResponseForUserData

@Serializable
data class SuccessResponseForUserData(
    val notificationId: String? = null,
    val creationTime: String? = null,
    val userName: String? = null,
    val primaryKey: String? = null,
    val email: String? = null,
    val gender: String? = null,
    val ageGroup: String? = null
) : ApiResponseForUserData()

@Serializable
data class ErrorResponseForUserData(
    val message: String? = null
) : ApiResponseForUserData()