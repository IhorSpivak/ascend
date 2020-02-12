package com.doneit.ascend.domain.gateway.gateway.data_source

import androidx.paging.PageKeyedDataSource
import com.doneit.ascend.domain.entity.SearchEntity
import com.doneit.ascend.domain.entity.common.ResponseEntity
import com.doneit.ascend.domain.entity.dto.SearchDTO
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
    private val requestDTO: SearchDTO
) : PageKeyedDataSource<Int, SearchEntity>() {

    private var lastMMPage: Int? = null

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, SearchEntity>
    ) {
        scope.launch {
            try {

                var page = 1
                var masterMindCount: Int? = null

                val masterMinds = remoteMasterMind.getMasterMindsList(requestDTO.toMasterMindRequest(page)).toResponseEntity(
                    {
                        masterMindCount = it?.count
                        it?.users?.map { groupIt -> groupIt.toEntity() }
                    },
                    {
                        it?.errors
                    }
                )

                val res = mutableListOf<SearchEntity>()

                if(masterMindCount != null) {
                    val perPage = requestDTO.perPage?:10
                    lastMMPage = ceil(masterMindCount!!.toDouble() / perPage).toInt()
                } else {
                    lastMMPage = 0
                }

                if (masterMinds.isSuccessful) {
                    res.addAll(masterMinds.successModel ?: listOf())
                }

                if(res.size < params.requestedLoadSize) {
                    val groups =
                        remoteGroup.getGroupsList(requestDTO.toGroupRequest(page)).toResponseEntity(
                            {
                                it?.groups?.map { groupIt -> groupIt.toEntity() }
                            },
                            {
                                it?.errors
                            }
                        )
                    if(groups.isSuccessful) {
                        res.addAll(groups.successModel ?: listOf())
                        page++
                    }
                }

                callback.onResult(res, null, page + 1)

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, SearchEntity>) {
        scope.launch {
            try {

                val res: ResponseEntity<List<SearchEntity>, List<String>>?
                if(params.key <= lastMMPage?:0) {
                    res =
                        remoteMasterMind.getMasterMindsList(requestDTO.toMasterMindRequest(params.key)).toResponseEntity(
                            {
                                it?.users?.map { groupIt -> groupIt.toEntity() }
                            },
                            {
                                it?.errors
                            }
                        )
                } else {
                    res =
                        remoteGroup.getGroupsList(requestDTO.toGroupRequest(params.key - lastMMPage!!)).toResponseEntity(
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