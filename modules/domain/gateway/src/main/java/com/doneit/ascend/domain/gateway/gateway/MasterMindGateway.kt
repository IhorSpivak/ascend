package com.doneit.ascend.domain.gateway.gateway

import androidx.paging.PagedList
import com.doneit.ascend.domain.entity.MasterMindEntity
import com.doneit.ascend.domain.entity.common.ResponseEntity
import com.doneit.ascend.domain.entity.dto.MasterMindListModel
import com.doneit.ascend.domain.gateway.common.mapper.toResponseEntity
import com.doneit.ascend.domain.gateway.common.mapper.to_entity.toEntity
import com.doneit.ascend.domain.gateway.common.mapper.to_remote.toRequest
import com.doneit.ascend.domain.gateway.gateway.base.BaseGateway
import com.doneit.ascend.domain.use_case.gateway.IMasterMindGateway
import com.doneit.ascend.source.storage.remote.repository.master_minds.IMasterMindRepository
import com.vrgsoft.networkmanager.NetworkManager

internal class MasterMindGateway(
    errors: NetworkManager,
    private val remote: IMasterMindRepository
) : BaseGateway(errors), IMasterMindGateway {
    override fun <T> calculateMessage(error: T): String {
        return ""//todo, not required for now
    }

    override suspend fun getMasterMindsList(listRequest: MasterMindListModel): ResponseEntity<List<MasterMindEntity>, List<String>> {
        return executeRemote { remote.getMasterMindsList(listRequest.toRequest()) }.toResponseEntity(
            {
                it?.users?.map { it.toEntity() }
            },
            {
                it?.errors
            }
        )
    }

    override suspend fun getMasterMindsPagedList(listRequest: MasterMindListModel): ResponseEntity<PagedList<MasterMindEntity>, List<String>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}