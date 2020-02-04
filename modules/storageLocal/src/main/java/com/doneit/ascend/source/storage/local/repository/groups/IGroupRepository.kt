package com.doneit.ascend.source.storage.local.repository.groups

import androidx.paging.DataSource
import com.doneit.ascend.source.storage.local.data.GroupLocal
import com.doneit.ascend.source.storage.local.data.dto.GroupFilter

interface IGroupRepository {
    fun getGroupList(filter: GroupFilter): DataSource.Factory<Int, GroupLocal>
    suspend fun getGroupById(id: Long): GroupLocal?
    suspend fun insertAll(groups: List<GroupLocal>)
    suspend fun update(group: GroupLocal)
    suspend fun remove(group: GroupLocal)
    suspend fun removeAll()
}