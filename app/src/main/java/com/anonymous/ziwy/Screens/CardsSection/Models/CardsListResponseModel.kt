package com.anonymous.ziwy.Screens.CardsSection.Models

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import kotlinx.serialization.Serializable

@Serializable
data class CardsListResponseModel(
    val statusCode: Int? = null,
    val body: Body? = null,
    val message: String? = null
)

@Serializable
data class Body(
    val cards: List<Card>? = null
)

@Serializable
data class Card(
    val cardBrand: String? = null,
    val cardID: String? = null,
    val cardName: String? = null,
    val cardType: String? = null,
    val isSelected : MutableState<Boolean> = mutableStateOf(false)
) {
    fun deepCopy(): Card {
        return Card(
            cardBrand = this.cardBrand,
            cardID = this.cardID,
            cardName = this.cardName,
            cardType = this.cardType,
            isSelected = mutableStateOf(this.isSelected.value)
        )
    }
}
