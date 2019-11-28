package com.doneit.ascend.source.storage.remote.repository.page

import com.doneit.ascend.source.storage.remote.api.PageApi
import com.doneit.ascend.source.storage.remote.data.response.PageResponse
import com.doneit.ascend.source.storage.remote.data.response.common.RemoteResponse
import com.doneit.ascend.source.storage.remote.data.response.errors.ErrorsListResponse
import com.doneit.ascend.source.storage.remote.repository.base.BaseRepository
import com.google.gson.Gson

internal class PageRepository(
    gson: Gson,
    private val api: PageApi
) : BaseRepository(gson), IPageRepository {

    override suspend fun getPage(pageType: String): RemoteResponse<PageResponse, ErrorsListResponse> {
        return execute({ api.getPageAsync(pageType) }, ErrorsListResponse::class.java)
    }
}