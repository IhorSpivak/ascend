package com.doneit.ascend.domain.gateway.gateway.boundaries

import com.doneit.ascend.domain.entity.dto.GroupListDTO
import com.doneit.ascend.domain.entity.group.GroupEntity
import com.doneit.ascend.domain.gateway.common.mapper.to_entity.toEntity
import com.doneit.ascend.domain.gateway.common.mapper.to_locale.toLocal
import com.doneit.ascend.domain.gateway.common.mapper.to_remote.toRequest
import com.doneit.ascend.source.storage.local.repository.groups.IGroupRepository
import kotlinx.coroutines.CoroutineScope

class GroupBoundaryCallback(
    scope: CoroutineScope,
    private val local: IGroupRepository,
    private val remote: com.doneit.ascend.source.storage.remote.repository.group.IGroupRepository,
    private val masterMindListModel: GroupListDTO
) : BaseBoundary<GroupEntity>(scope) {

    override suspend fun fetchPage() {
        val response = remote.getGroupsList(masterMindListModel.toRequest(pageIndexToLoad))
        if (response.isSuccessful) {
            val model = response.successModel!!.groups.map { it.toEntity() }

            val loadedCount = model.size
            val remoteCount = response.successModel!!.count

            receivedItems(loadedCount, remoteCount)

            local.insertAll(model.map { it.toLocal() })
        }
    }
}