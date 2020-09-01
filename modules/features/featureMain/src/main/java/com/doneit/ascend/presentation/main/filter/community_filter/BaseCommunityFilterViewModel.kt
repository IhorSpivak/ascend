package com.doneit.ascend.presentation.main.filter.community_filter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.doneit.ascend.domain.entity.user.Community
import com.doneit.ascend.presentation.main.filter.FilterViewModel
import java.util.*

abstract class BaseCommunityFilterViewModel<T : CommunityFilterModel> :
    FilterViewModel<T>(), CommunityFilterAbstractContract.ViewModel<T>{
    override val communities: LiveData<List<String>> = MutableLiveData(Community.values().map { it.title })

    override fun communitySelected(community: String) {
        filter.community = Community.valueOf(community.toUpperCase(Locale.getDefault()))
    }
}