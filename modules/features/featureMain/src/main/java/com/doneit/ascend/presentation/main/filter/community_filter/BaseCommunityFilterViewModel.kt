package com.doneit.ascend.presentation.main.filter.community_filter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.doneit.ascend.domain.entity.user.Community
import com.doneit.ascend.presentation.main.filter.FilterViewModel
import com.doneit.ascend.presentation.utils.append

abstract class BaseCommunityFilterViewModel<T : CommunityFilterModel> :
    FilterViewModel<T>(), CommunityFilterAbstractContract.ViewModel<T>{
    override val communities: LiveData<List<Community>> = MutableLiveData(Community.values().toList())

    override fun communitySelected(community: Community, isChecked: Boolean) {
        if(isChecked) {
            filter.community = filter.community.orEmpty().append(community)
        }else {
            filter.community = filter.community?.filter { it != community }
            if(filter.community.isNullOrEmpty()) filter.community = null
        }
    }

    override fun setFilter(filter: T) {
        super.setFilter(filter)
        this.filter.community = filter.community
    }
}