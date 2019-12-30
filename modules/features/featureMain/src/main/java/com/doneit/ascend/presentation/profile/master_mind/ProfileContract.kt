package com.doneit.ascend.presentation.profile.master_mind

interface ProfileContract {
    interface ViewModel : com.doneit.ascend.presentation.profile.common.ProfileContract.ViewModel {
        fun updateDisplayName(newDisplayName: String)
        fun updateShortDescription(newShortDescription: String)

        fun navigateToEditBio()
    }

    interface Router {
        fun navigateToEditBio()
    }
}