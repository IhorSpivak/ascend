package com.doneit.ascend.presentation.main.filter.group_type_filter

import com.doneit.ascend.domain.entity.group.GroupType
import com.doneit.ascend.presentation.main.filter.community_filter.BaseCommunityFilterViewModel
import com.vrgsoft.annotations.CreateFactory
import com.vrgsoft.annotations.ViewModelDiModule

@CreateFactory
@ViewModelDiModule
class GroupTypeFilterViewModel : BaseCommunityFilterViewModel<GroupTypeFilterModel>(),
    GroupTypeFilterContract.ViewModel {

    override fun initFilterModel(): GroupTypeFilterModel {
        return GroupTypeFilterModel()
    }

    override fun setFilter(filter: GroupTypeFilterModel) {
        super.setFilter(filter)
        this.filter.groupType = filter.groupType
    }

    override fun setGroupType(groupType: GroupType) {
        filter.groupType = groupType
    }
}