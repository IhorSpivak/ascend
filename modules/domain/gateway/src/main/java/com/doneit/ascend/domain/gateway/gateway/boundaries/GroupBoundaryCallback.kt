package com.doneit.ascend.domain.gateway.gateway.boundaries

import androidx.paging.PagedList
import com.doneit.ascend.domain.entity.dto.GroupListModel
import com.doneit.ascend.domain.entity.group.GroupEntity
import com.doneit.ascend.domain.gateway.common.mapper.to_entity.toEntity
import com.doneit.ascend.domain.gateway.common.mapper.to_locale.toLocal
import com.doneit.ascend.domain.gateway.common.mapper.to_remote.toRequest
import com.doneit.ascend.source.storage.local.repository.groups.IGroupRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GroupBoundaryCallback(
    private val scope: CoroutineScope,
    private val local: IGroupRepository,
    private val remote: com.doneit.ascend.source.storage.remote.repository.group.IGroupRepository,
    private val masterMindListModel: GroupListModel
) : PagedList.BoundaryCallback<GroupEntity>() {

    private var remoteCount = 0
    private var loadedCount = 0
    private var pageIndexToLoad = 0

    fun loadInitial() {
        fetchPage()
    }

    //It isn't used because of unsuitable invoke cases
    override fun onZeroItemsLoaded() {
    }

    override fun onItemAtEndLoaded(itemAtEnd: GroupEntity) {
        if (loadedCount < remoteCount) {
            fetchPage()
        }
    }

    override fun onItemAtFrontLoaded(itemAtFront: GroupEntity) {
    }

    private fun fetchPage() {
        scope.launch(Dispatchers.IO) {
            val response = remote.getGroupsList(masterMindListModel.toRequest(pageIndexToLoad))
            if (response.isSuccessful) {
                remoteCount = response.successModel!!.count
                val model = response.successModel!!.groups.map { it.toEntity() }

                pageIndexToLoad += 1
                loadedCount += model.size

                local.insertAll(model.map { it.toLocal() })
            }
        }
    }
}