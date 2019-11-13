package com.vrgsoft.carButler.domain.gateway.gateway.genre

import com.vrgsoft.carButler.source.storage.remote.common.error.ConnectionError
import com.vrgsoft.carButler.source.storage.remote.common.result.BaseResult
import com.vrgsoft.carButler.source.storage.remote.common.result.ErrorResult
import com.vrgsoft.carButler.source.storage.remote.common.result.SuccessResult
import com.vrgsoft.carButler.source.storage.remote.data.response.GenreResponse
import com.vrgsoft.carButler.source.storage.remote.repository.genre.IGenreRepository

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