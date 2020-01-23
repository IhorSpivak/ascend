package com.doneit.ascend.presentation.main.groups.common

import com.doneit.ascend.domain.entity.dto.GroupStatus
import com.doneit.ascend.domain.entity.dto.SortType
import com.doneit.ascend.presentation.main.groups.GroupsArg
import com.doneit.ascend.presentation.main.groups.group_list.GroupListArgs


fun GroupsArg.toGroupListArgs(sortType: SortType, status: GroupStatus): GroupListArgs {
    return GroupListArgs(
        sortType,
        userId,
        groupType,
        isMyGroups,
        status
    )
}