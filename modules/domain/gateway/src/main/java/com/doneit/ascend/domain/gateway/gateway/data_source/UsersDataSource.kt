package com.doneit.ascend.domain.gateway.gateway.data_source

import android.util.Patterns
import androidx.paging.PageKeyedDataSource
import com.doneit.ascend.domain.entity.AttendeeEntity
import com.doneit.ascend.domain.gateway.common.mapper.to_entity.toEntity
import com.doneit.ascend.source.storage.remote.data.request.SearchUserRequest
import com.doneit.ascend.source.storage.remote.repository.group.IGroupRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.runBlocking

class UsersDataSource(
    private val scope: CoroutineScope,
    private val remoteGroup: IGroupRepository,
    private val searchQuery: String,
    private val userId: Long,
    private val memberList: List<AttendeeEntity>?
) : PageKeyedDataSource<Int, AttendeeEntity>() {

    private var loadCount: Int = 0
    private var loaded: Int = 0

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, AttendeeEntity>
    ) {
        runBlocking {
            if (searchQuery.length <= 1) {
                callback.onResult(emptyList(), null, 2)
                return@runBlocking
            }
            val data = remoteGroup.searchUsers(searchQuery.toSearchRequest(1))
            if (data.isSuccessful) {
                data.successModel?.apply {
                    loadCount = count
                    loaded = users.size
                }
                if (memberList == null) {
                    callback.onResult(data.successModel?.users?.filter { it.id != userId }?.map {
                        it.toEntity()
                    } ?: emptyList(), null, 2)
                } else {
                    callback.onResult(data.successModel?.users?.filter {
                        var result = true
                        memberList.firstOrNull { member -> member.id == it.id }?.let {
                            result = false
                        }
                        result
                    }?.map {
                        it.toEntity()
                    } ?: emptyList(), null, 2)
                }
            }
        }
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, AttendeeEntity>) {
        if (loadCount > loaded) {
            runBlocking {
                if (searchQuery.length <= 1) {
                    callback.onResult(emptyList(), params.key.inc())
                    return@runBlocking
                }
                val page = params.key
                val data = remoteGroup.searchUsers(searchQuery.toSearchRequest(page))
                if (data.isSuccessful) {
                    if (memberList == null) {
                        callback.onResult(data.successModel?.users?.filter { it.id != userId }
                            ?.map { it.toEntity() }
                            ?: emptyList(), params.key.inc())
                    } else {
                        callback.onResult(data.successModel?.users?.filter {
                            var result = true
                            memberList.firstOrNull { member -> member.id == it.id }
                                ?.let {
                                    result = false
                                }
                            result
                        }?.map { it.toEntity() }
                            ?: emptyList(), params.key.inc())
                    }
                }
            }
        }
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, AttendeeEntity>) {

    }

    private fun String.toSearchRequest(page: Int): SearchUserRequest {

        return if (Patterns.EMAIL_ADDRESS.matcher(this).matches()) {
            SearchUserRequest(
                page = page,
                email = this
            )
        } else {
            SearchUserRequest(
                page = page,
                fullName = this
            )
        }
    }
}