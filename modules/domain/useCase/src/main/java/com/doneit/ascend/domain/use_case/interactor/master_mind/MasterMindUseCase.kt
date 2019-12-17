package com.doneit.ascend.domain.use_case.interactor.master_mind

import androidx.paging.PagedList
import com.doneit.ascend.domain.entity.MasterMindEntity
import com.doneit.ascend.domain.entity.PageEntity
import com.doneit.ascend.domain.entity.common.ResponseEntity

interface MasterMindUseCase {
    suspend fun getDafaultMasterMindList(): ResponseEntity<List<MasterMindEntity>, List<String>>
    suspend fun getMasterMindList(isFollowed: Boolean): PagedList<MasterMindEntity>
}