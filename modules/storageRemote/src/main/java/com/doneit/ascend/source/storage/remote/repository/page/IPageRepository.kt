package com.doneit.ascend.source.storage.remote.repository.page

import com.doneit.ascend.source.storage.remote.data.response.PageResponse
import com.doneit.ascend.source.storage.remote.data.response.common.RemoteResponse
import com.doneit.ascend.source.storage.remote.data.response.errors.ErrorsListResponse

interface IPageRepository {
    suspend fun getPage(pageType: String): RemoteResponse<PageResponse, ErrorsListResponse>
}