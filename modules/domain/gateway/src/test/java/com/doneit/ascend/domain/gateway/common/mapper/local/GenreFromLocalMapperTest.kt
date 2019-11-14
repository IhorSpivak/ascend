package com.doneit.ascend.domain.gateway.common.mapper.local

import com.doneit.ascend.source.storage.local.data.GenreLocal
import org.junit.Assert
import org.junit.Test

class GenreFromLocalMapperTest {
    @Test
    fun toGenre() {
        val local = GenreLocal().apply {
            id = 1
            name = "name"
            image = "image"
        }

        val entity = local.toGenre()

        Assert.assertEquals(1, entity.id)
        Assert.assertEquals("name", entity.name)
        Assert.assertEquals("image", entity.image)
    }
}