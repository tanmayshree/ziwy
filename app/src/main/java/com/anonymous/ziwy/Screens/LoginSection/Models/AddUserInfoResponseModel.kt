package com.anonymous.ziwy.Screens.LoginSection.Models

import kotlinx.serialization.Serializable

@Serializable
data class AddUserInfoResponseModel(
    val statusCode: Int? = null,
    val body: Body? = null
)

@Serializable
data class Body(
    val message: String? = null,
    val user: User? = null
)

@Serializable
data class User(
    val primaryKey: String? = null,
    val email: String? = null,
    val userName: String? = null,
    val gender: String? = null,
    val ageGroup: String? = null,
    val notificationId: String? = null,
    val creationTime: String? = null
)