package com.doneit.ascend.source.storage.remote.data.response.common

data class RemoteResponse<T, E>(
    val isSuccessful: Boolean,
    val code: Int,
    val message: String,
    val successModel: T?,
    val errorModel: E?
)