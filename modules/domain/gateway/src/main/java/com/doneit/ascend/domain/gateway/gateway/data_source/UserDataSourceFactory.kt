package com.doneit.ascend.domain.gateway.gateway.data_source

import androidx.paging.DataSource
import com.doneit.ascend.domain.entity.AttendeeEntity
import com.doneit.ascend.source.storage.remote.repository.group.IGroupRepository
import kotlinx.coroutines.CoroutineScope

class UserDataSourceFactory(
    private val scope: CoroutineScope,
    private val remoteGroup: IGroupRepository,
    private val searchQuery: String
): DataSource.Factory<Int, AttendeeEntity> (){
    override fun create(): DataSource<Int, AttendeeEntity> {
        return UsersDataSource(scope, remoteGroup, searchQuery)
    }
}