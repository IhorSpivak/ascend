package com.doneit.ascend.presentation.profile.regular_user

interface UserProfileContract {
    interface ViewModel : com.doneit.ascend.presentation.profile.common.ProfileContract.ViewModel {
        fun onAgeClick()
    }

    interface Router {
        fun navigateToSetAge()
    }
}