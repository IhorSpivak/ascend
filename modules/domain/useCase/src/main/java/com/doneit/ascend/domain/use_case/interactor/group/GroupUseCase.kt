package com.doneit.ascend.domain.use_case.interactor.group

import androidx.paging.PagedList
import com.doneit.ascend.domain.entity.GroupEntity
import com.doneit.ascend.domain.entity.common.ResponseEntity
import com.doneit.ascend.domain.entity.dto.CreateGroupModel
import com.doneit.ascend.domain.entity.dto.GroupCredentialsModel
import com.doneit.ascend.domain.entity.dto.GroupListModel
import com.doneit.ascend.domain.entity.dto.SubscribeGroupModel

interface GroupUseCase {
    suspend fun createGroup(groupModel: CreateGroupModel): ResponseEntity<GroupEntity, List<String>>

    suspend fun getGroupList(model: GroupListModel): ResponseEntity<List<GroupEntity>, List<String>>

    suspend fun getGroupListPaged(model: GroupListModel): PagedList<GroupEntity>

    suspend fun getGroupDetails(groupId: Long): ResponseEntity<GroupEntity, List<String>>

    suspend fun deleteGroup(groupId: Long): ResponseEntity<Unit, List<String>>

    suspend fun subscribe(model: SubscribeGroupModel): ResponseEntity<Unit, List<String>>

    suspend fun getCredentials(groupId: Long): ResponseEntity<GroupCredentialsModel, List<String>>
}