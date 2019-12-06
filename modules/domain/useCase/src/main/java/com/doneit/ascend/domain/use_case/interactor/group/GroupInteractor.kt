package com.doneit.ascend.domain.use_case.interactor.group

import com.doneit.ascend.domain.entity.CreateGroupModel
import com.doneit.ascend.domain.entity.GroupEntity
import com.doneit.ascend.domain.entity.common.RequestEntity
import com.doneit.ascend.domain.use_case.gateway.IGroupGateway

internal class GroupInteractor(
    private val groupGateway: IGroupGateway
) : GroupUseCase {

    override suspend fun createGroup(groupModel: CreateGroupModel): RequestEntity<GroupEntity, List<String>> {
        return groupGateway.createGroup(groupModel)
    }
}