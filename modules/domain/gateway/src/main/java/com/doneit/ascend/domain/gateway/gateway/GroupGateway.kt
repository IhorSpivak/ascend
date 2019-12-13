package com.doneit.ascend.domain.gateway.gateway

import androidx.paging.PagedList
import com.doneit.ascend.domain.entity.GroupEntity
import com.doneit.ascend.domain.entity.common.ResponseEntity
import com.doneit.ascend.domain.entity.dto.CreateGroupModel
import com.doneit.ascend.domain.entity.dto.GroupListModel
import com.doneit.ascend.domain.gateway.common.mapper.toResponseEntity
import com.doneit.ascend.domain.gateway.common.mapper.to_entity.toEntity
import com.doneit.ascend.domain.gateway.common.mapper.to_remote.toCreateGroupRequest
import com.doneit.ascend.domain.gateway.common.mapper.to_remote.toRequest
import com.doneit.ascend.domain.gateway.gateway.base.BaseGateway
import com.doneit.ascend.domain.use_case.gateway.IGroupGateway
import com.doneit.ascend.source.storage.remote.repository.group.IGroupRepository
import com.vrgsoft.networkmanager.NetworkManager
import kotlinx.coroutines.GlobalScope
import java.io.File

internal class GroupGateway(
    errors: NetworkManager,
    private val remote: IGroupRepository
) : BaseGateway(errors), IGroupGateway {

    override fun <T> calculateMessage(error: T): String {
        return ""//todo, not required for now
    }

    override suspend fun createGroup(groupModel: CreateGroupModel): ResponseEntity<GroupEntity, List<String>> {
        return executeRemote {
            remote.createGroup(
                File(groupModel.imagePath),
                groupModel.toCreateGroupRequest()
            )
        }.toResponseEntity(
            {
                it?.toEntity()
            },
            {
                it?.errors
            }
        )
    }

    override suspend fun getGroupsList(groupListModel: GroupListModel): ResponseEntity<List<GroupEntity>, List<String>> {
        return remote.getGroupsList(groupListModel.toRequest()).toResponseEntity(
            {
                it?.groups?.map { it.toEntity() }
            },
            {
                it?.errors
            }
        )
    }

    override suspend fun getGroupsListPaged(groupListModel: GroupListModel): PagedList<GroupEntity> {

        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(groupListModel.perPage?:10)
            .build()

        val dataSource = GroupDataSource(GlobalScope, remote, groupListModel)
        val executor = MainThreadExecutor()

        return PagedList.Builder<Int, GroupEntity>(dataSource, config)
            .setFetchExecutor(executor)
            .setNotifyExecutor(executor)
            .build()
    }
}