package com.doneit.ascend.presentation.main.home.groups_list

import com.doneit.ascend.domain.entity.dto.GroupListDTO
import com.doneit.ascend.presentation.main.filter.FilterFragment
import com.doneit.ascend.presentation.main.filter.FilterType
import com.doneit.ascend.presentation.main.filter.community_filter.CommunityFilterFragment
import com.doneit.ascend.presentation.main.filter.community_filter.CommunityFilterModel
import com.doneit.ascend.presentation.main.filter.group_type_filter.GroupTypeFilterFragment
import com.doneit.ascend.presentation.main.filter.group_type_filter.GroupTypeFilterModel
import com.doneit.ascend.presentation.main.filter.tag_filter.TagFilterFragment
import com.doneit.ascend.presentation.main.filter.tag_filter.TagFilterModel

object FilterFactory {
    fun create(dto: GroupListDTO?, type: FilterType): FilterFragment<*> {
        return when(type){
            FilterType.COMMUNITY -> CommunityFilterFragment.newInstance(CommunityFilterModel.create(dto))
            FilterType.TAG -> TagFilterFragment.newInstance(TagFilterModel.create(dto))
            FilterType.GROUP_TYPE -> GroupTypeFilterFragment.newInstance(GroupTypeFilterModel.create(dto))
        }
    }
}