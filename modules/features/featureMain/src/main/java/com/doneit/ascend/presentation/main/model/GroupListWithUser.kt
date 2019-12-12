package com.doneit.ascend.presentation.main.model

import com.doneit.ascend.domain.entity.GroupEntity
import com.doneit.ascend.domain.entity.UserEntity

data class GroupListWithUser(
    val groups: List<GroupEntity>?,
    val user: UserEntity
)