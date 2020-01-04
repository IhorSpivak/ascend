package com.doneit.ascend.presentation.profile.change_location

interface ChangeLocationContract {
    interface ViewModel: com.doneit.ascend.presentation.profile.common.ProfileContract.ViewModel {
        fun updateLocation(city: String, country: String)
        fun goBack()
    }

    interface Router {
        fun onBack()
    }
}