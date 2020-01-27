package com.doneit.ascend.source.storage.local.repository.master_minds

import androidx.paging.DataSource
import com.doneit.ascend.source.storage.local.data.MasterMindLocal
import com.doneit.ascend.source.storage.local.data.dto.MMFilter

interface IMasterMindRepository {
    fun getMMList(filter: MMFilter): DataSource.Factory<Int, MasterMindLocal>
    suspend fun getMMById(id: Long): MasterMindLocal?
    suspend fun insertAll(masterMinds: List<MasterMindLocal>)
    suspend fun update(masterMind: MasterMindLocal)
    suspend fun remove(masterMind: MasterMindLocal)
    suspend fun removeAll()
}