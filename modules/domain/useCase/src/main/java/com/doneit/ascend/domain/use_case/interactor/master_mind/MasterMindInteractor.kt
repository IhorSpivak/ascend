package com.doneit.ascend.domain.use_case.interactor.master_mind

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.doneit.ascend.domain.entity.MasterMindEntity
import com.doneit.ascend.domain.entity.common.ResponseEntity
import com.doneit.ascend.domain.entity.dto.MasterMindListModel
import com.doneit.ascend.domain.use_case.gateway.IMasterMindGateway

internal class MasterMindInteractor(
    private val masterMindGateway: IMasterMindGateway
) : MasterMindUseCase {

    override suspend fun getDefaultMasterMindList(): ResponseEntity<List<MasterMindEntity>, List<String>> {
        return masterMindGateway.getMasterMindsList(MasterMindListModel(followed = true))
    }

    override fun getMasterMindList(isFollowed: Boolean?): LiveData<PagedList<MasterMindEntity>> {
        return masterMindGateway.getMasterMindsPagedList(MasterMindListModel(followed = isFollowed))
    }

    override fun getMMListByName(fullName: String): LiveData<PagedList<MasterMindEntity>> {
        return masterMindGateway.getMasterMindsPagedList(MasterMindListModel(fullName = fullName, followed = false))
    }

    override suspend fun getProfile(id: Long): ResponseEntity<MasterMindEntity, List<String>> {
        return masterMindGateway.getProfile(id)
    }

    override suspend fun follow(userId: Long): ResponseEntity<Unit, List<String>> {
        return masterMindGateway.follow(userId)
    }

    override suspend fun unfollow(userId: Long): ResponseEntity<Unit, List<String>> {
        return masterMindGateway.unfollow(userId)
    }

    override suspend fun setRating(userId: Long, rating: Int): ResponseEntity<Unit, List<String>> {
        return masterMindGateway.setRating(userId, rating)
    }

    override suspend fun sendReport(
        userId: Long,
        content: String
    ): ResponseEntity<Unit, List<String>> {
        return masterMindGateway.sendReport(userId, content)
    }
}