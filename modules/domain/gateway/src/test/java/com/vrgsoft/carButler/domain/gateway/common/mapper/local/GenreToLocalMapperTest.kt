package com.vrgsoft.carButler.domain.gateway.common.mapper.local

import com.vrgsoft.carButler.domain.entity.Genre
import org.junit.Assert.assertEquals
import org.junit.Test

class GenreToLocalMapperTest {
    @Test
    fun toGenreLocal() {
        val entity = Genre().apply {
            id = 1
            name = "name"
            image = "image"
        }

        val local = entity.toLocal()

        assertEquals(1, local.id)
        assertEquals("name", local.name)
        assertEquals("image", local.image)
    }
}