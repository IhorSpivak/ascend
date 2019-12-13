package com.doneit.ascend.presentation.main.models

import androidx.paging.PagedList
import com.doneit.ascend.domain.entity.GroupEntity
import com.doneit.ascend.domain.entity.UserEntity

data class GroupListWithUserPaged(
    val groups: PagedList<GroupEntity>?,
    val user: UserEntity
)