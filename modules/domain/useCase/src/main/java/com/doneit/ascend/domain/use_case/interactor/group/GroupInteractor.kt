package com.doneit.ascend.domain.use_case.interactor.group

import androidx.paging.PagedList
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

    override suspend fun getGroupList(model: GroupListModel): ResponseEntity<List<GroupEntity>, List<String>> {
        return groupGateway.getGroupsList(model)
    }

    override suspend fun getGroupListPaged(model: GroupListModel): PagedList<GroupEntity> {
        return groupGateway.getGroupsListPaged(model)
    }

    override suspend fun getGroupDetails(groupId: Long): ResponseEntity<GroupEntity, List<String>> {
        return groupGateway.getGroupDetails(groupId)
    }

    override suspend fun deleteGroup(groupId: Long): ResponseEntity<Unit, List<String>> {
        return groupGateway.deleteGroup(groupId)
    }
}