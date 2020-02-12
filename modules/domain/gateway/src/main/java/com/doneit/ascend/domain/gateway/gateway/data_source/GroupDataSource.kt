package com.doneit.ascend.domain.gateway.gateway.data_source

import androidx.paging.PageKeyedDataSource
import com.doneit.ascend.domain.entity.group.GroupEntity
import com.doneit.ascend.domain.entity.dto.GroupListDTO
import com.doneit.ascend.domain.gateway.common.mapper.toResponseEntity
import com.doneit.ascend.domain.gateway.common.mapper.to_entity.toEntity
import com.doneit.ascend.domain.gateway.common.mapper.to_remote.toRequest
import com.doneit.ascend.source.storage.remote.repository.group.IGroupRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class GroupDataSource(
    private val scope: CoroutineScope,
    private val remote: IGroupRepository,
    private val groupListModel: GroupListDTO
) : PageKeyedDataSource<Int, GroupEntity>() {

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, GroupEntity>
    ) {
        scope.launch {
            try {

                val page = 1

                val groups = remote.getGroupsList(groupListModel.toRequest(page)).toResponseEntity(
                    {
                        it?.groups?.map { groupIt -> groupIt.toEntity() }
                    },
                    {
                        it?.errors
                    }
                )

                if (groups.isSuccessful) {
                    callback.onResult(groups.successModel ?: listOf(), null, page + 1)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, GroupEntity>) {
        scope.launch {
            try {

                val page = params.key

                val groups =
                    remote.getGroupsList(groupListModel.toRequest(page)).toResponseEntity(
                        {
                            it?.groups?.map { groupIt -> groupIt.toEntity() }
                        },
                        {
                            it?.errors
                        }
                    )

                if (groups.isSuccessful) {
                    callback.onResult(groups.successModel ?: listOf(), page + 1)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, GroupEntity>) {
    }
}