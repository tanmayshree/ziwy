package com.anonymous.ziwy.Screens.CardsSection.ViewModel

import android.net.Uri
import com.anonymous.ziwy.GenericModels.LoadingScreenState
import com.anonymous.ziwy.Screens.CardsSection.Models.Card
import com.anonymous.ziwy.Screens.HomeSection.Models.Coupon

data class CardsStore(
    val isLoading: Boolean? = null,
    val loadingScreenState: LoadingScreenState? = null,
    val message: String? = null,
    val cardsList: List<Card>? = null,
    val userCardsList: List<String>? = null,

    val isFetchingCardListSuccess: Boolean? = null,
    val username: String? = null,
    val countryCode: String? = null,
    val phoneNumber: String? = null,

    val isCardsUpdatedSuccessfully: Boolean? = null,
)
