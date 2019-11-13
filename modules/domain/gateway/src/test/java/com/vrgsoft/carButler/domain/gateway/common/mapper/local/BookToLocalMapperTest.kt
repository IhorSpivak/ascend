package com.vrgsoft.carButler.domain.gateway.common.mapper.local

import org.junit.Assert.assertEquals
import org.junit.Test

class BookToLocalMapperTest {

    @Test
    fun toBookLocal() {
        val entity = Book().apply {
            id = 1
            title = "title"
            description = "desc"
            imageUrl = "image"
            favorite = true
            totalTime = 1488
            readingProgress = 12

            reader = Reader().apply {
                id = 2
            }

            author = Author().apply {
                id = 2
            }

            tracks = listOf(
                Track().apply {
                    id = 1
                    url = "url1"
                    time = 1
                },
                Track().apply {
                    id = 2
                    url = "url2"
                    time = 2
                }
            )
        }

        val local = entity.toBookLocal()
        val book = local.bookEntity
        val tracks = local.tracks

        assertEquals(1, book.id)
        assertEquals("title", book.title)
        assertEquals("desc", book.description)
        assertEquals("image", book.imageUrl)
        assertEquals(true, book.favorite)
        assertEquals(1488, book.totalTime)
        assertEquals(12, book.readingProgress)
        assertEquals(2L, book.author.id)
        assertEquals(2L, book.reader.id)

        assertEquals(2, tracks.size)
        assertEquals(1L, tracks[0].id)
        assertEquals(2L, tracks[1].id)
    }

    @Test
    fun toAuthorLocal() {
        val entity = Author().apply {
            id = 1
            booksCount = 5
            name = "name"
        }

        val local = entity.toAuthorLocal()

        assertEquals(1L, local.id)
        assertEquals(5, local.booksCount)
        assertEquals("name", local.name)
    }

    @Test
    fun toSerieLocal() {
        val entity = Serie().apply {
            id = 1
            name = "name"
        }

        val local = entity.toSerieLocal()

        assertEquals(1L, local.id)
        assertEquals("name", local.name)
    }

    @Test
    fun toReaderLocal() {
        val entity = Reader().apply {
            id = 1
            booksCount = 5
            name = "name"
        }

        val local = entity.toReaderLocal()

        assertEquals(1L, local.id)
        assertEquals(5, local.booksCount)
        assertEquals("name", local.name)
    }

    @Test
    fun toTrackLocal() {
        val entity = Track().apply {
            id = 1
            time = 4
            url = "url"
        }

        val bookId = 1L

        val local = entity.toLocal(bookId)

        assertEquals(1L, local.id)
        assertEquals(4L, local.time)
        assertEquals("url", local.url)
        assertEquals(bookId, local.bookId)
    }
}