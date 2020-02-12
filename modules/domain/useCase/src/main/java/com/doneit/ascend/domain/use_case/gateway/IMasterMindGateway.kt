package com.doneit.ascend.domain.use_case.gateway

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.doneit.ascend.domain.entity.MasterMindEntity
import com.doneit.ascend.domain.entity.common.ResponseEntity
import com.doneit.ascend.domain.entity.dto.MasterMindListDTO

interface IMasterMindGateway {
    suspend fun getMasterMindsList(listRequest: MasterMindListDTO): ResponseEntity<List<MasterMindEntity>, List<String>>
    fun getMasterMindsPagedList(listRequest: MasterMindListDTO): LiveData<PagedList<MasterMindEntity>>
    fun getProfile(id: Long): LiveData<MasterMindEntity>
    suspend fun follow(userId: Long): ResponseEntity<Unit, List<String>>
    suspend fun unfollow(userId: Long): ResponseEntity<Unit, List<String>>
    suspend fun setRating(userId: Long, rating: Int): ResponseEntity<Unit, List<String>>
    suspend fun sendReport(userId: Long, content: String): ResponseEntity<Unit, List<String>>
}