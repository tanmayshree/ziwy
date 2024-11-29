package com.anonymous.ziwy.Screens.LoginSection.ViewModel

import android.net.Uri
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.anonymous.ziwy.GenericModels.AppUpdateInfoResponseModel
import com.anonymous.ziwy.GenericModels.LoadingScreenState
import com.anonymous.ziwy.Screens.LoginSection.Models.SuccessResponseForUserData

data class LoginStore(
    val isLoading: Boolean? = null,
    val loadingScreenState: LoadingScreenState? = null,
    val message: String? = null,
//    val countryList : List<String>? = null,
    val isLoginSuccess: Boolean? = null,

    val isNewUser: Boolean? = null,
    val userData: SuccessResponseForUserData? = null,
    val token: String = "",

    val phoneNumber: MutableState<String> = mutableStateOf(""),
    val countryCode: MutableState<String> = mutableStateOf("91"),
    val countryFlag: MutableState<String> = mutableStateOf("🇮🇳"),

    val imageUri: Uri? = null,
    val isGoogleSignInCompleted: Boolean? = null,
    val appUpdateInfo: AppUpdateInfoResponseModel? = null
)
