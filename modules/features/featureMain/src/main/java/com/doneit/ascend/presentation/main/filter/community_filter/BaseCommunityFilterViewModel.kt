package com.doneit.ascend.presentation.main.filter.community_filter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.doneit.ascend.domain.entity.user.Community
import com.doneit.ascend.presentation.main.filter.FilterViewModel
import java.util.*

abstract class BaseCommunityFilterViewModel<T : CommunityFilterModel> :
    FilterViewModel<T>(), CommunityFilterAbstractContract.ViewModel<T>{
    override val communities: LiveData<List<Community>> = MutableLiveData(Community.values().toList())

    override fun communitySelected(community: Community) {
        filter.community = community
    }
}