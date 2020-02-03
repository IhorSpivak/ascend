package com.doneit.ascend.source.storage.local.repository.groups

import androidx.paging.DataSource
import androidx.sqlite.db.SimpleSQLiteQuery
import com.doneit.ascend.source.storage.local.data.GroupLocal
import com.doneit.ascend.source.storage.local.data.dto.GroupFilter


internal class GroupRepository(
    private val dao: GroupDao
) : IGroupRepository {

    override fun getGroupList(filter: GroupFilter): DataSource.Factory<Int, GroupLocal> {
        return when(filter.sortType) {
            0 -> dao.getAllASC(filter.groupStatus)
            else -> dao.getAllDESC(filter.groupStatus)
        }
    }

    override suspend fun getGroupById(id: Long): GroupLocal? {
        return dao.getById(id)
    }

    override suspend fun insertAll(groups: List<GroupLocal>) {
        dao.insertAll(groups)
    }

    override suspend fun update(group: GroupLocal) {
        dao.update(group)
    }

    override suspend fun remove(group: GroupLocal) {
        dao.remove(group)
    }

    override suspend fun removeAll() {
        dao.removeAll()
    }
}