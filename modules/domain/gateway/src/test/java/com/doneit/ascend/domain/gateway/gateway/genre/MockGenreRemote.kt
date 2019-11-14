package com.doneit.ascend.domain.gateway.gateway.genre

import com.doneit.ascend.source.storage.remote.common.error.ConnectionError
import com.doneit.ascend.source.storage.remote.common.result.BaseResult
import com.doneit.ascend.source.storage.remote.common.result.ErrorResult
import com.doneit.ascend.source.storage.remote.common.result.SuccessResult
import com.doneit.ascend.source.storage.remote.data.response.GenreResponse
import com.doneit.ascend.source.storage.remote.repository.genre.IGenreRepository

class MockGenreRemote(private val type: Type) : IGenreRepository {
    override suspend fun getList(): BaseResult<List<GenreResponse>> {
        return when (type) {
            Type.MULTI -> SuccessResult(
                listOf(
                    GenreResponse(),
                    GenreResponse(),
                    GenreResponse()
                )
            )
            Type.EMPTY -> SuccessResult(listOf())
            Type.ERROR -> ErrorResult(ConnectionError())
        }
    }

    enum class Type {
        EMPTY, MULTI, ERROR
    }
}