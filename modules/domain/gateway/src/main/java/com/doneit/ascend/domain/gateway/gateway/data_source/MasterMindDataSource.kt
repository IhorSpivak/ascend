package com.doneit.ascend.domain.gateway.gateway.data_source

import androidx.paging.PageKeyedDataSource
import com.doneit.ascend.domain.entity.MasterMindEntity
import com.doneit.ascend.domain.entity.common.ResponseEntity
import com.doneit.ascend.domain.entity.dto.MasterMindListModel
import com.doneit.ascend.domain.gateway.common.mapper.toResponseEntity
import com.doneit.ascend.domain.gateway.common.mapper.to_entity.toEntity
import com.doneit.ascend.domain.gateway.common.mapper.to_remote.toRequest
import com.doneit.ascend.source.storage.remote.repository.master_minds.IMasterMindRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class MasterMindDataSource(
    private val scope: CoroutineScope,
    private val remote: IMasterMindRepository,
    private val masterMindListModel: MasterMindListModel
) : PageKeyedDataSource<Int, MasterMindEntity>() {

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, MasterMindEntity>
    ) {
        scope.launch {
            try {

                val page = 1

                val result = loadMasterMindList(page)

                if (result.isSuccessful) {
                    callback.onResult(result.successModel ?: listOf(), null, page + 1)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, MasterMindEntity>) {
        scope.launch {
            try {

                val page = params.key

                val result = loadMasterMindList(page)

                if (result.isSuccessful) {
                    callback.onResult(result.successModel ?: listOf(), page + 1)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun loadBefore(
        params: LoadParams<Int>,
        callback: LoadCallback<Int, MasterMindEntity>
    ) {
    }

    private suspend fun loadMasterMindList(page: Int): ResponseEntity<List<MasterMindEntity>, List<String>> {

        return remote.getMasterMindsList(masterMindListModel.toRequest(page)).toResponseEntity(
            {
                it?.users?.map { groupIt -> groupIt.toEntity() }
            },
            {
                it?.errors
            }
        )
    }
}