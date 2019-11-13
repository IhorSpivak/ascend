package com.vrgsoft.carButler.domain.gateway.gateway.genre

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.vrgsoft.carButler.source.storage.local.data.GenreLocal
import com.vrgsoft.carButler.source.storage.local.repository.genre.IGenreRepository
import kotlinx.coroutines.test.runBlockingTest

class MockGenreLocal(private val type: Type) : IGenreRepository {
    private val genresLive = MutableLiveData<List<GenreLocal>>()

    override suspend fun getGenres(): List<GenreLocal> {
        return when (type) {
            Type.MULTI -> listOf(
                GenreLocal(),
                GenreLocal(),
                GenreLocal()
            )
            Type.EMPTY -> listOf()
        }
    }

    override fun getGenresLive(): LiveData<List<GenreLocal>> {
        return genresLive.apply {
            runBlockingTest {
                postValue(getGenres())
            }
        }
    }

    override suspend fun saveGenres(list: List<GenreLocal>) {
        val current = ArrayList(genresLive.value ?: listOf())
        current.addAll(list)
        genresLive.postValue(current)
    }

    enum class Type {
        EMPTY, MULTI
    }
}