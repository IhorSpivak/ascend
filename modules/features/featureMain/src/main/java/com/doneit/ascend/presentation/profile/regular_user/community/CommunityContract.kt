package com.doneit.ascend.presentation.profile.regular_user.community

import androidx.lifecycle.LiveData
import com.doneit.ascend.presentation.main.base.BaseViewModel
import com.doneit.ascend.presentation.models.PresentationCommunityModel

interface CommunityContract {
    interface ViewModel : BaseViewModel {
        val questions: LiveData<List<PresentationCommunityModel>>
        val canSave: LiveData<Boolean>

        fun setSelectedCommunity(community: String)
        fun saveCommunity()
        fun goBack()
    }
}