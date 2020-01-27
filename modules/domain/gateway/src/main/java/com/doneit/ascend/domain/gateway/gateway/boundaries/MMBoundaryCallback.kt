package com.doneit.ascend.domain.gateway.gateway.boundaries

import androidx.paging.PagedList
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
    private val scope: CoroutineScope,
    private val local: IMasterMindRepository,
    private val remote: com.doneit.ascend.source.storage.remote.repository.master_minds.IMasterMindRepository,
    private val masterMindListModel: MasterMindListModel
) : PagedList.BoundaryCallback<MasterMindEntity>() {

    private var remoteCount = 0
    private var loadedCount = 0
    private var pageIndexToLoad = 0

    fun loadInitial() {
        fetchPage()
    }

    //It isn't used because of unsuitable invoke cases
    override fun onZeroItemsLoaded() {
    }

    override fun onItemAtEndLoaded(itemAtEnd: MasterMindEntity) {
        if (loadedCount < remoteCount) {
            fetchPage()
        }
    }

    override fun onItemAtFrontLoaded(itemAtFront: MasterMindEntity) {
    }

    private fun fetchPage() {
        scope.launch(Dispatchers.IO) {
            val response = remote.getMasterMindsList(masterMindListModel.toRequest(pageIndexToLoad))
            if (response.isSuccessful) {
                remoteCount = response.successModel!!.count
                val model = response.successModel!!.users.map { it.toEntity() }

                pageIndexToLoad += 1
                loadedCount += model.size

                local.insertAll(model.map { it.toLocal() })
            }
        }
    }
}