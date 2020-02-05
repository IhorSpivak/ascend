package com.doneit.ascend.source.storage.local.repository.master_minds

import androidx.paging.DataSource
import com.doneit.ascend.source.storage.local.data.MasterMindLocal
import com.doneit.ascend.source.storage.local.data.dto.MMFilter

internal class MasterMindRepository(
    private val dao: MasterMindDao
) : IMasterMindRepository {

    override fun getMMList(filter: MMFilter): DataSource.Factory<Int, MasterMindLocal> {
        return when {
            filter.followed == true -> dao.getFollowed()
            filter.followed == false -> dao.getUnFollowed()
            else -> dao.getAll()
        }
    }

    override suspend fun getMMById(id: Long): MasterMindLocal? {
        return dao.getById(id)
    }

    override suspend fun insertAll(masterMinds: List<MasterMindLocal>) {
        dao.insertAll(masterMinds)
    }

    override suspend fun update(masterMind: MasterMindLocal) {
        dao.update(masterMind)
    }

    override suspend fun remove(masterMind: MasterMindLocal) {
        dao.remove(masterMind)
    }

    override suspend fun removeAll() {
        dao.removeAll()
    }
}