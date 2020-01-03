package com.doneit.ascend.presentation.profile.master_mind

interface MMProfileContract {
    interface ViewModel : com.doneit.ascend.presentation.profile.common.ProfileContract.ViewModel {
        fun updateDisplayName(newDisplayName: String)
        fun updateShortDescription(newShortDescription: String)

        fun navigateToEditBio()
        fun navigateToRatings()
    }

    interface Router {
        fun navigateToEditBio()
        fun navigateToRatings()
    }
}