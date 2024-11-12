package com.anonymous.ziwy.Screens.CouponDetailSection.ViewModel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class CouponDetailViewModelFactory(
    private val application: Application,
) : ViewModelProvider.AndroidViewModelFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CouponDetailViewModel::class.java)) {
            return CouponDetailViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
