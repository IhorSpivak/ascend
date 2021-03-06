package com.doneit.ascend.presentation.models.group

import com.doneit.ascend.domain.entity.group.GroupEntity
import com.doneit.ascend.domain.entity.user.UserEntity

data class GroupListWithUser(
    val groups: List<GroupEntity>,
    val user: UserEntity
)