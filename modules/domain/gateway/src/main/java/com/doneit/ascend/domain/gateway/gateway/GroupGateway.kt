package com.doneit.ascend.domain.gateway.gateway

import com.doneit.ascend.domain.entity.CreateGroupModel
import com.doneit.ascend.domain.entity.GroupEntity
import com.doneit.ascend.domain.entity.common.RequestEntity
import com.doneit.ascend.domain.gateway.common.mapper.toRequestEntity
import com.doneit.ascend.domain.gateway.common.mapper.to_entity.toEntity
import com.doneit.ascend.domain.gateway.common.mapper.to_remote.toCreateGroupRequest
import com.doneit.ascend.domain.gateway.gateway.base.BaseGateway
import com.doneit.ascend.domain.use_case.gateway.IGroupGateway
import com.doneit.ascend.source.storage.remote.repository.group.IGroupRepository
import com.vrgsoft.networkmanager.NetworkManager
import java.io.File

internal class GroupGateway(
    errors: NetworkManager,
    private val remote: IGroupRepository
) : BaseGateway(errors), IGroupGateway {

    override fun <T> calculateMessage(error: T): String {
        return ""//todo, not required for now
    }

    override suspend fun createGroup(groupModel: CreateGroupModel): RequestEntity<GroupEntity, List<String>> {
        return executeRemote { remote.createGroup(File(groupModel.imagePath), groupModel.toCreateGroupRequest()) }.toRequestEntity(
            {
                it?.toEntity()
            },
            {
                it?.errors
            }
        )
    }
}