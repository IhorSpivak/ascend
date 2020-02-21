package com.doneit.ascend.presentation.models.group

import com.doneit.ascend.domain.entity.UserEntity
import com.doneit.ascend.domain.entity.group.GroupEntity

data class GroupListWithUser(
    val groups: List<GroupEntity>,
    val user: UserEntity
)