package com.doneit.ascend.domain.use_case.interactor.master_mind

import androidx.paging.PagedList
import com.doneit.ascend.domain.entity.MasterMindEntity
import com.doneit.ascend.domain.entity.common.ResponseEntity

interface MasterMindUseCase {
    suspend fun getDefaultMasterMindList(): ResponseEntity<List<MasterMindEntity>, List<String>>
    suspend fun getMasterMindList(isFollowed: Boolean?): PagedList<MasterMindEntity>
    suspend fun getProfile(id: Long): ResponseEntity<MasterMindEntity, List<String>>
    suspend fun follow(userId: Long): ResponseEntity<Unit, List<String>>
    suspend fun unfollow(userId: Long): ResponseEntity<Unit, List<String>>
    suspend fun setRating(userId: Long, rating: Int): ResponseEntity<Unit, List<String>>
    suspend fun sendReport(userId: Long, content: String): ResponseEntity<Unit, List<String>>
}