package com.doneit.ascend.domain.use_case.interactor.master_mind

import androidx.paging.PagedList
import com.doneit.ascend.domain.entity.MasterMindEntity
import com.doneit.ascend.domain.entity.common.ResponseEntity
import com.doneit.ascend.domain.entity.dto.MasterMindListModel
import com.doneit.ascend.domain.use_case.gateway.IMasterMindGateway

internal class MasterMindInteractor(
    private val groupGateway: IMasterMindGateway
) : MasterMindUseCase {

    override suspend fun getDafaultMasterMindList(): ResponseEntity<List<MasterMindEntity>, List<String>> {
        return groupGateway.getMasterMindsList(MasterMindListModel(followed = true))
    }

    override suspend fun getMasterMindList(isFollowed: Boolean): ResponseEntity<PagedList<MasterMindEntity>, List<String>> {
        return groupGateway.getMasterMindsPagedList(MasterMindListModel(followed = isFollowed))
    }
}