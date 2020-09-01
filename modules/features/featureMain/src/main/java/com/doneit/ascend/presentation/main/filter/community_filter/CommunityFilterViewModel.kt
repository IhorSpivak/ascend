package com.doneit.ascend.presentation.main.filter.community_filter

import com.vrgsoft.annotations.CreateFactory
import com.vrgsoft.annotations.ViewModelDiModule

@CreateFactory
@ViewModelDiModule
class CommunityFilterViewModel : BaseCommunityFilterViewModel<CommunityFilterModel>(),
    CommunityFilterContract.ViewModel {

    override fun initFilterModel(): CommunityFilterModel {
        return CommunityFilterModel()
    }
}