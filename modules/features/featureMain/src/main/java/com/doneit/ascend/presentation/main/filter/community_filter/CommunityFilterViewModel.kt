package com.doneit.ascend.presentation.main.filter.community_filter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.doneit.ascend.domain.entity.user.Community
import com.doneit.ascend.presentation.main.filter.FilterViewModel
import com.vrgsoft.annotations.CreateFactory
import com.vrgsoft.annotations.ViewModelDiModule
import java.util.*

@CreateFactory
@ViewModelDiModule
class CommunityFilterViewModel : FilterViewModel<CommunityFilterModel>(),
    CommunityFilterContract.ViewModel {

    override val communities: LiveData<List<String>> = MutableLiveData(Community.values().map { it.title })

    override fun communitySelected(community: String) {
        filter.community = Community.valueOf(community.toUpperCase(Locale.getDefault()))
    }

    override fun initFilterModel(): CommunityFilterModel {
        return CommunityFilterModel()
    }
}