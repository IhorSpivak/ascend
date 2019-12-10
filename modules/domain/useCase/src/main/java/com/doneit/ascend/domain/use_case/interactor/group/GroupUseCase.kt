package com.doneit.ascend.domain.use_case.interactor.group

import com.doneit.ascend.domain.entity.dto.CreateGroupModel
import com.doneit.ascend.domain.entity.GroupEntity
import com.doneit.ascend.domain.entity.common.ResponseEntity

interface GroupUseCase  {
    suspend fun createGroup(groupModel: CreateGroupModel): ResponseEntity<GroupEntity, List<String>>

    suspend fun getDefaultGroupList(): ResponseEntity<List<GroupEntity>, List<String>>
}