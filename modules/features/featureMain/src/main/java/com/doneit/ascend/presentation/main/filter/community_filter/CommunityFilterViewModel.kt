package com.doneit.ascend.presentation.main.filter.community_filter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.doneit.ascend.domain.entity.user.Community
import com.vrgsoft.annotations.CreateFactory
import com.vrgsoft.annotations.ViewModelDiModule

@CreateFactory
@ViewModelDiModule
class CommunityFilterViewModel : BaseCommunityFilterViewModel<CommunityFilterModel>(),
    CommunityFilterContract.ViewModel {

    override val communities: LiveData<List<Community>> = MutableLiveData(Community.values().toList())

    override fun initFilterModel(): CommunityFilterModel {
        return CommunityFilterModel()
    }
}