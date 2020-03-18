package com.doneit.ascend.domain.gateway.gateway.data_source

import android.util.Patterns
import androidx.paging.PageKeyedDataSource
import com.doneit.ascend.domain.entity.AttendeeEntity
import com.doneit.ascend.domain.entity.dto.UserSearchDTO
import com.doneit.ascend.domain.gateway.common.mapper.to_entity.toEntity
import com.doneit.ascend.source.storage.remote.data.request.SearchUserRequest
import com.doneit.ascend.source.storage.remote.repository.group.IGroupRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class UsersDataSource(
    private val scope: CoroutineScope,
    private val remoteGroup: IGroupRepository,
    private val searchQuery: String
) : PageKeyedDataSource<Int, AttendeeEntity>() {

    private var loadCount: Int = 0
    private var loaded: Int = 0

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, AttendeeEntity>
    ) {
        runBlocking {
            val data = remoteGroup.searchUsers(searchQuery.toSearchRequest(1))
            if (data.isSuccessful){
                data.successModel?.apply {
                    loadCount = count
                    loaded = users.size
                }
                callback.onResult(data.successModel?.users?.map {
                    it.toEntity() }?: emptyList(), null, 2)
            }
        }
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, AttendeeEntity>) {
        if (loadCount > loaded){
            runBlocking {
                val data = remoteGroup.searchUsers(searchQuery.toSearchRequest(1))
                if (data.isSuccessful){
                    callback.onResult(data.successModel?.users?.map { it.toEntity() }?: emptyList(), params.key.inc())
                }
            }
        }
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, AttendeeEntity>) {

    }

    private fun String.toSearchRequest(page: Int): SearchUserRequest{

        return if (Patterns.EMAIL_ADDRESS.matcher(this).matches()){
            SearchUserRequest(
                page = page,
                email = this
            )
        }else{
            SearchUserRequest(
                page = page,
                fullName = this
            )
        }
    }
}