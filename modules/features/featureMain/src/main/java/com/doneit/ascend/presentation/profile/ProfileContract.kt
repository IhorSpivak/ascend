package com.doneit.ascend.presentation.profile

import com.doneit.ascend.presentation.main.base.BaseViewModel

interface ProfileContract {
    interface ViewModel : BaseViewModel {
        fun signOut()
        fun deleteAccount()
    }

    interface Router {
        fun navigateToLogin()
    }
}