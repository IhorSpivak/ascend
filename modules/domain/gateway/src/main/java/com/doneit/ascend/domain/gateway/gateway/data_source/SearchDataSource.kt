package com.doneit.ascend.domain.gateway.gateway.data_source

import androidx.paging.PageKeyedDataSource
import com.doneit.ascend.domain.entity.SearchEntity
import com.doneit.ascend.domain.entity.common.ResponseEntity
import com.doneit.ascend.domain.entity.dto.SearchModel
import com.doneit.ascend.domain.gateway.common.mapper.toResponseEntity
import com.doneit.ascend.domain.gateway.common.mapper.to_entity.toEntity
import com.doneit.ascend.domain.gateway.common.mapper.to_remote.toGroupRequest
import com.doneit.ascend.domain.gateway.common.mapper.to_remote.toMasterMindRequest
import com.doneit.ascend.source.storage.remote.repository.group.IGroupRepository
import com.doneit.ascend.source.storage.remote.repository.master_minds.IMasterMindRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.math.ceil

class SearchDataSource(
    private val scope: CoroutineScope,
    private val remoteGroup: IGroupRepository,
    private val remoteMasterMind: IMasterMindRepository,
    private val requestModel: SearchModel
) : PageKeyedDataSource<Int, SearchEntity>() {

    private var lastGroupPage: Int? = null

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, SearchEntity>
    ) {
        scope.launch {
            try {

                val page = 1
                var masterMindCount: Int? = null

                val groups = remoteMasterMind.getMasterMindsList(requestModel.toMasterMindRequest(page)).toResponseEntity(
                    {
                        masterMindCount = it?.count
                        it?.users?.map { groupIt -> groupIt.toEntity() }
                    },
                    {
                        it?.errors
                    }
                )

                if(masterMindCount != null) {
                    val perPage = requestModel.perPage?:10
                    lastGroupPage = ceil(masterMindCount!!.toDouble() / perPage).toInt()
                } else {
                    lastGroupPage = 0
                }

                if (groups.isSuccessful) {
                    callback.onResult(groups.successModel ?: listOf(), null, page + 1)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, SearchEntity>) {
        scope.launch {
            try {

                var res: ResponseEntity<List<SearchEntity>, List<String>>?
                if(params.key <= lastGroupPage?:0) {
                    res =
                        remoteMasterMind.getMasterMindsList(requestModel.toMasterMindRequest(params.key)).toResponseEntity(
                            {
                                it?.users?.map { groupIt -> groupIt.toEntity() }
                            },
                            {
                                it?.errors
                            }
                        )
                } else {
                    res =
                        remoteGroup.getGroupsList(requestModel.toGroupRequest(params.key - lastGroupPage!!)).toResponseEntity(
                            {
                                it?.groups?.map { groupIt -> groupIt.toEntity() }
                            },
                            {
                                it?.errors
                            }
                        )
                }



                if (res.isSuccessful) {
                    callback.onResult(res.successModel ?: listOf(), params.key + 1)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, SearchEntity>) {
    }
}