package com.doneit.ascend.presentation.main.filter.group_type_filter

import com.doneit.ascend.domain.entity.group.GroupType
import com.doneit.ascend.presentation.main.filter.community_filter.CommunityFilterAbstractContract

interface GroupTypeFilterContract {
    interface ViewModel : CommunityFilterAbstractContract.ViewModel<GroupTypeFilterModel> {
        fun setGroupType(groupType: GroupType)
    }
}