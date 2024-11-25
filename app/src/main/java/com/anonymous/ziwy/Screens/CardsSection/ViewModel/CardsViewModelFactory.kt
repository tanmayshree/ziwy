package com.anonymous.ziwy.Screens.CardsSection.ViewModel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class CardsViewModelFactory(
    private val application: Application,
) : ViewModelProvider.AndroidViewModelFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CardsViewModel::class.java)) {
            return CardsViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
