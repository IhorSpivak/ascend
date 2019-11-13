package com.vrgsoft.carButler.domain.gateway.common.mapper.remote

import com.vrgsoft.carButler.source.storage.remote.data.response.GenreResponse
import org.junit.Assert
import org.junit.Test

class GenreFromRemoteMapperTest {
    @Test
    fun toGenre() {
        val remote = GenreResponse(
            id = 1,
            name = "name",
            image = "image"
        )

        val entity = remote.toGenre()

        Assert.assertEquals(1, entity.id)
        Assert.assertEquals("name", entity.name)
        Assert.assertEquals("image", entity.image)
    }

    @Test
    fun toGenreFromNull() {
        val remote = GenreResponse(
            id = null,
            name = null,
            image = null
        )

        val entity = remote.toGenre()

        Assert.assertEquals(-1L, entity.id)
        Assert.assertEquals("", entity.name)
        Assert.assertEquals("", entity.image)
    }
}