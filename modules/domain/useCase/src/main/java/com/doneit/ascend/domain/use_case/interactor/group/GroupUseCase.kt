package com.doneit.ascend.domain.use_case.interactor.group

import com.doneit.ascend.domain.entity.CreateGroupModel
import com.doneit.ascend.domain.entity.GroupEntity
import com.doneit.ascend.domain.entity.common.RequestEntity

interface GroupUseCase  {
    suspend fun createGroup(groupModel: CreateGroupModel): RequestEntity<GroupEntity, List<String>>
}