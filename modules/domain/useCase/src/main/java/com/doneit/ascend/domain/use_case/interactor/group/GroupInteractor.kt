package com.doneit.ascend.domain.use_case.interactor.group

import com.doneit.ascend.domain.entity.GroupEntity
import com.doneit.ascend.domain.entity.common.ResponseEntity
import com.doneit.ascend.domain.entity.dto.CreateGroupModel
import com.doneit.ascend.domain.entity.dto.GroupListModel
import com.doneit.ascend.domain.use_case.gateway.IGroupGateway

internal class GroupInteractor(
    private val groupGateway: IGroupGateway
) : GroupUseCase {

    override suspend fun createGroup(groupModel: CreateGroupModel): ResponseEntity<GroupEntity, List<String>> {
        return groupGateway.createGroup(groupModel)
    }

    override suspend fun getDefaultGroupList(): ResponseEntity<List<GroupEntity>, List<String>> {
        return groupGateway.getGroupsList(GroupListModel(
            myGroups = true
        ))
    }

    override suspend fun getGroupList(model: GroupListModel): ResponseEntity<List<GroupEntity>, List<String>> {
        return groupGateway.getGroupsList(model)
    }
}