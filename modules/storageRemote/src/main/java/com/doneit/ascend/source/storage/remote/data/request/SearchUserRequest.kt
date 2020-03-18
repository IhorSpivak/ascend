package com.doneit.ascend.source.storage.remote.data.request

data class SearchUserRequest(
    val page: Int? = null,
    val perPage: Int? = null,
    val sortColumn: String? = null,
    val sortType: String? = null,
    val fullName: String? = null,
    val email: String? = null
)