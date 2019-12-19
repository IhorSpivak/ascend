package com.doneit.ascend.domain.use_case.gateway

import androidx.paging.PagedList
import com.doneit.ascend.domain.entity.MasterMindEntity
import com.doneit.ascend.domain.entity.common.ResponseEntity
import com.doneit.ascend.domain.entity.dto.MasterMindListModel

interface IMasterMindGateway {
    suspend fun getMasterMindsList(listRequest: MasterMindListModel): ResponseEntity<List<MasterMindEntity>, List<String>>
    suspend fun getMasterMindsPagedList(listRequest: MasterMindListModel): PagedList<MasterMindEntity>
    suspend fun getProfile(id: Long): ResponseEntity<MasterMindEntity, List<String>>
}