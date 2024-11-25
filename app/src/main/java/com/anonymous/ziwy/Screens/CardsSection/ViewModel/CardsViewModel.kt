package com.anonymous.ziwy.Screens.CardsSection.ViewModel

import android.app.Application
import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.anonymous.ziwy.GenericModels.LoadingScreenState
import com.anonymous.ziwy.Screens.CardsSection.Models.UpdateUserCardRequestModel
import com.anonymous.ziwy.Utilities.Retrofit.Repository
import com.anonymous.ziwy.Utilities.Retrofit.Resource
import com.anonymous.ziwy.Utilities.Utils
import com.anonymous.ziwy.Utilities.ZConstants
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CardsViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val _state = MutableStateFlow(CardsStore())
    val state: StateFlow<CardsStore> = _state

    private val repository = Repository()

    fun clearMessage() {
        _state.value = _state.value.copy(
            message = null
        )
    }

    fun getUserData(context: Context) {
        viewModelScope.launch {
            _state.value = _state.value.copy(
                loadingScreenState = LoadingScreenState(
                    isLoading = true,
                    screen = ZConstants.FETCH_CARDS_LIST
                ),
                isFetchingCardListSuccess = null
            )
            delay(1000L)
            val userDetails = Utils.getUserDetailsFromPreferences(context)
            _state.value = _state.value.copy(
                username = userDetails.username,
                countryCode = userDetails.countryCode,
                phoneNumber = userDetails.phoneNumber,
            )
            println("620555 MainViewModel getUserData Success $userDetails")
            fetchCardsList(userDetails.phoneNumber, userDetails.countryCode)
        }
    }

    private fun fetchCardsList(mobileNumber: String?, countryCode: String?) {
        viewModelScope.launch {
            repository.getCardsList(mobileNumber, countryCode).collect { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        _state.value = _state.value.copy(
                            isLoading = true,
                            loadingScreenState = LoadingScreenState(
                                isLoading = true,
                                screen = ZConstants.FETCH_CARDS
                            )
                        )
                        println("620555 MainViewModel fetchCouponsList Loading...")
                    }

                    is Resource.Success -> {

                        val cardsList = resource.data?.body?.cards ?: emptyList()

                        _state.value = _state.value.copy(
                            isLoading = false,
                            loadingScreenState = LoadingScreenState(
                                isLoading = false,
                                screen = ZConstants.FETCH_CARDS
                            ),
                            cardsList = cardsList,
//                            message = "Coupons fetched successfully"
                        )
                        println("620555 MainViewModel fetchCouponsList Success ${resource.data}")
                        getUserCardList(mobileNumber, countryCode)
                    }

                    is Resource.Error -> {
                        _state.value = _state.value.copy(
                            isLoading = false,
                            loadingScreenState = LoadingScreenState(
                                isLoading = false,
                                screen = ZConstants.FETCH_CARDS
                            ),
                            cardsList = emptyList(),
                        )
                        _state.value = _state.value.copy(
                            loadingScreenState = LoadingScreenState(
                                isLoading = false,
                                screen = ZConstants.FETCH_CARDS_LIST,
                            ),
                            isFetchingCardListSuccess = false
                        )
                        println("620555 MainViewModel fetchCouponsList Error ${resource.message}")
                    }
                }
            }
        }

    }

    private fun getUserCardList(mobileNumber: String?, countryCode: String?) {
        viewModelScope.launch {
            repository.getUserCardList(mobileNumber, countryCode).collect { resource ->

                when (resource) {
                    is Resource.Loading -> {
                        _state.value = _state.value.copy(
                            isLoading = true,
                            loadingScreenState = LoadingScreenState(
                                isLoading = true,
                                screen = ZConstants.FETCH_USER_CARDS
                            )
                        )
                    }

                    is Resource.Success -> {
                        val userCardsList = resource.data?.body ?: emptyList()

                        val cardsList = state.value.cardsList?.map {
                            if (userCardsList.contains(it.cardID)) {
                                it.copy(isSelected = mutableStateOf(true))
                            } else {
                                it
                            }
                        }

                        _state.value = _state.value.copy(
                            isLoading = false,
                            loadingScreenState = LoadingScreenState(
                                isLoading = false,
                                screen = ZConstants.FETCH_USER_CARDS
                            ),
                            userCardsList = userCardsList,
                            cardsList = cardsList
                        )
                        _state.value = _state.value.copy(
                            loadingScreenState = LoadingScreenState(
                                isLoading = false,
                                screen = ZConstants.FETCH_CARDS_LIST
                            ),
                            isFetchingCardListSuccess = true
                        )
                    }

                    is Resource.Error -> {
                        _state.value = _state.value.copy(
                            isLoading = false,
                            loadingScreenState = LoadingScreenState(
                                isLoading = false,
                                screen = ZConstants.FETCH_USER_CARDS
                            ),
                            userCardsList = emptyList(),
                            message = resource.message
                        )
                        _state.value = _state.value.copy(
                            loadingScreenState = LoadingScreenState(
                                isLoading = false,
                                screen = ZConstants.FETCH_CARDS_LIST
                            ),
                            isFetchingCardListSuccess = false
                        )
                    }
                }

            }
        }
    }

    fun updateUserCardList(
        userCardsList: List<String?>
    ) {
        viewModelScope.launch {
            repository.updateUserCardList(
                UpdateUserCardRequestModel(
                    mobileNumber = _state.value.phoneNumber,
                    countryCode = state.value.countryCode,
                    cardID = userCardsList.filterNotNull()
                )
            ).collect { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        _state.value = _state.value.copy(
                            loadingScreenState = LoadingScreenState(
                                isLoading = true,
                                screen = ZConstants.UPDATE_USER_CARDS
                            )
                        )
                    }

                    is Resource.Success -> {
                        _state.value = _state.value.copy(
                            loadingScreenState = LoadingScreenState(
                                isLoading = false,
                                screen = ZConstants.UPDATE_USER_CARDS
                            ),
                            userCardsList = userCardsList.filterNotNull(),
                            message = "Cards selected successfully",
                            isCardsUpdatedSuccessfully = true
                        )
                    }

                    is Resource.Error -> {
                        _state.value = _state.value.copy(
                            loadingScreenState = LoadingScreenState(
                                isLoading = false,
                                screen = ZConstants.UPDATE_USER_CARDS
                            ),
                            message = resource.message,
                            isCardsUpdatedSuccessfully = false
                        )
                    }
                }

            }
        }
    }

    fun setSelectedCards(selectedCardsList: List<String?>) {
        val cardsList = state.value.cardsList?.map {
            if (selectedCardsList.contains(it.cardID)) {
                it.copy(isSelected = mutableStateOf(true))
            } else {
                it
            }
        }
        _state.value = _state.value.copy(
            cardsList = cardsList
        )
    }

}
