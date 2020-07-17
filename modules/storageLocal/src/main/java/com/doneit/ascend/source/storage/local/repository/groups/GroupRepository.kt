package com.doneit.ascend.source.storage.local.repository.groups

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import com.doneit.ascend.source.storage.local.data.GroupLocal
import com.doneit.ascend.source.storage.local.data.dto.GroupFilter


internal class GroupRepository(
    private val dao: GroupDao
) : IGroupRepository {

    override suspend fun getGroupList(filter: GroupFilter): DataSource.Factory<Int, GroupLocal> {
        return when (filter.sortType) {
            0 -> filter.groupType?.let { dao.getAllByTypeASC(filter.groupStatus, type = filter.groupType) } ?: dao.getAllASC(filter.groupStatus)
            else -> filter.groupType?.let { dao.getAllByTypeDESC(filter.groupStatus, type = filter.groupType) } ?: dao.getAllDESC(filter.groupStatus)
        }
    }

    override suspend fun getUpcomingGroupList(filter: GroupFilter): DataSource.Factory<Int, GroupLocal> {
        return if(filter.isUpcoming!!) {
            dao.getUpcomingASC(filter.userId!!)
        } else {
            dao.getAllDESC(filter.groupStatus)
        }
    }

    override suspend fun getGroupById(id: Long): GroupLocal? {
        return dao.getById(id)
    }

    override fun getGroupByIdLive(id: Long): LiveData<GroupLocal?> {
        return dao.getByIdLive(id)
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

    override suspend fun removeAllByType(type: Int) {
        dao.removeAllByType(type)
    }

    override suspend fun removeAll() {
        dao.removeAll()
    }
}