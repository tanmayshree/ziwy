package com.anonymous.ziwy.GenericModels

import kotlinx.serialization.Serializable

@Serializable
data class AppUpdateInfoResponseModel(
    val isUpdateRequired: Boolean? = null,
    val currentAppVersion: String? = null
)
