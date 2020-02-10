package com.doneit.ascend.domain.gateway.gateway.boundaries

import com.doneit.ascend.domain.entity.MasterMindEntity
import com.doneit.ascend.domain.entity.dto.MasterMindListModel
import com.doneit.ascend.domain.gateway.common.mapper.to_entity.toEntity
import com.doneit.ascend.domain.gateway.common.mapper.to_locale.toLocal
import com.doneit.ascend.domain.gateway.common.mapper.to_remote.toRequest
import com.doneit.ascend.source.storage.local.repository.master_minds.IMasterMindRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MMBoundaryCallback(
    scope: CoroutineScope,
    private val local: IMasterMindRepository,
    private val remote: com.doneit.ascend.source.storage.remote.repository.master_minds.IMasterMindRepository,
    private val masterMindListModel: MasterMindListModel
) : BaseBoundary<MasterMindEntity>(scope) {

    override suspend fun fetchPage() {
        val response = remote.getMasterMindsList(masterMindListModel.toRequest(pageIndexToLoad))
        if (response.isSuccessful) {
            val model = response.successModel!!.users.map { it.toEntity() }

            val loadedCount = model.size
            val remoteCount = response.successModel!!.count

            receivedItems(loadedCount, remoteCount)

            local.insertAll(model.map { it.toLocal() })
        }
    }
}