package com.doneit.ascend.presentation.main.filter.community_filter

import androidx.lifecycle.LiveData
import com.doneit.ascend.domain.entity.user.Community
import com.doneit.ascend.presentation.main.filter.FilterContract

interface CommunityFilterAbstractContract {
    interface ViewModel<T : CommunityFilterModel> : FilterContract.ViewModel<T> {
        val communities: LiveData<List<Community>>

        fun communitySelected(community: Community)
    }
}

interface CommunityFilterContract {
    interface ViewModel : CommunityFilterAbstractContract.ViewModel<CommunityFilterModel>
}