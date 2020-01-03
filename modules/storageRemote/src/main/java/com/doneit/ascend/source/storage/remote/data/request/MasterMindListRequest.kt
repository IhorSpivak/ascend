package com.doneit.ascend.source.storage.remote.data.request

import com.doneit.ascend.source.storage.remote.data.request.base.BasePagedModel

//used like dto
class MasterMindListRequest(
    page: Int?,
    perPage: Int?,
    sortColumn: String?,
    sortType: String?,
    val fullName: String?,
    val displayName: String?,
    val followed: Boolean?,
    val rated: Boolean?
) : BasePagedModel(page, perPage, sortColumn, sortType)