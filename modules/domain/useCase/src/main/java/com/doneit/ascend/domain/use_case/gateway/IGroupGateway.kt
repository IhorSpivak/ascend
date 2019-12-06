package com.doneit.ascend.domain.use_case.gateway

import com.doneit.ascend.domain.entity.CreateGroupModel
import com.doneit.ascend.domain.entity.GroupEntity
import com.doneit.ascend.domain.entity.common.RequestEntity

interface IGroupGateway {
    suspend fun createGroup(groupModel: CreateGroupModel): RequestEntity<GroupEntity, List<String>>
}