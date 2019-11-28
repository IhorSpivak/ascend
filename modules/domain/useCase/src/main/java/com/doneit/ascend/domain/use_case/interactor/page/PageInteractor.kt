package com.doneit.ascend.domain.use_case.interactor.page

import com.doneit.ascend.domain.entity.PageEntity
import com.doneit.ascend.domain.entity.common.RequestEntity
import com.doneit.ascend.domain.use_case.gateway.IPageGateway

internal class PageInteractor(
    private val pageGateway: IPageGateway
) : PageUseCase {
    override suspend fun getPage(pageType: String): RequestEntity<PageEntity, List<String>> {
        return pageGateway.getPage(pageType)
    }
}