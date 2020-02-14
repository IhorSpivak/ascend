package com.doneit.ascend.presentation.models.group

import androidx.paging.PagedList
import com.doneit.ascend.domain.entity.group.GroupEntity
import com.doneit.ascend.domain.entity.UserEntity

data class GroupListWithUserPaged(
    val groups: PagedList<GroupEntity>?,
    val user: UserEntity
)