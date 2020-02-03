package com.doneit.ascend.presentation.models

import com.doneit.ascend.domain.entity.group.GroupEntity
import com.doneit.ascend.domain.entity.UserEntity

data class GroupListWithUser(
    val groups: List<GroupEntity>?,
    val user: UserEntity
)