package com.doneit.ascend.domain.gateway.gateway

import com.doneit.ascend.domain.entity.PageEntity
import com.doneit.ascend.domain.entity.common.ResponseEntity
import com.doneit.ascend.domain.gateway.common.mapper.toResponseEntity
import com.doneit.ascend.domain.gateway.common.mapper.to_entity.toEntity
import com.doneit.ascend.domain.gateway.gateway.base.BaseGateway
import com.doneit.ascend.domain.use_case.gateway.IPageGateway
import com.doneit.ascend.source.storage.remote.repository.page.IPageRepository
import com.vrgsoft.networkmanager.NetworkManager

internal class PageGateway(
    errors: NetworkManager,
    private val remote: IPageRepository
) : BaseGateway(errors), IPageGateway {

    override fun <T> calculateMessage(error: T): String {
        return ""//todo, not required for now
    }

    override suspend fun getPage(pageType: String): ResponseEntity<PageEntity, List<String>> {
        return executeRemote { remote.getPage(pageType) }.toResponseEntity({
            it?.toEntity()
        }, {
            it?.errors
        })
    }
}