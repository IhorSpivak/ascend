package com.doneit.ascend.domain.gateway.common.mapper.local

import com.doneit.ascend.source.storage.local.data.SerieLocal
import com.doneit.ascend.source.storage.local.data.TrackLocal
import com.doneit.ascend.source.storage.local.data.author.AuthorLocal
import com.doneit.ascend.source.storage.local.data.book.BookDTO
import com.doneit.ascend.source.storage.local.data.book.BookLocal
import com.doneit.ascend.source.storage.local.data.reader.ReaderLocal
import org.junit.Assert
import org.junit.Test

class BookFromLocalMapperTest {

    @Test
    fun toBookEntity() {
        val local = BookDTO().apply {
            bookEntity = BookLocal().apply {
                id = 1
                title = "title"
                description = "desc"
                imageUrl = "image"
                favorite = true
                totalTime = 1488
                readingProgress = 12

                reader = ReaderLocal().apply {
                    id = 2
                }

                author = AuthorLocal().apply {
                    id = 2
                }

                serie = SerieLocal().apply {
                    id = 2
                }
            }

            tracks = listOf(
                TrackLocal().apply {
                    id = 1
                    url = "url1"
                    time = 1
                },
                TrackLocal().apply {
                    id = 2
                    url = "url2"
                    time = 2
                }
            )
        }

        val entity = local.toBook()
        val tracks = entity.tracks

        Assert.assertEquals(1, entity.id)
        Assert.assertEquals("title", entity.title)
        Assert.assertEquals("desc", entity.description)
        Assert.assertEquals("image", entity.imageUrl)
        Assert.assertEquals(true, entity.favorite)
        Assert.assertEquals(1488, entity.totalTime)
        Assert.assertEquals(12, entity.readingProgress)
        Assert.assertEquals(2L, entity.author.id)
        Assert.assertEquals(2L, entity.reader.id)

        Assert.assertEquals(2, tracks.size)
        Assert.assertEquals(1L, tracks[0].id)
        Assert.assertEquals(2L, tracks[1].id)
    }

    @Test
    fun toAuthor() {
        val local = AuthorLocal().apply {
            id = 1
            booksCount = 5
            name = "name"
        }

        val entity = local.toAuthor()

        Assert.assertEquals(1L, entity.id)
        Assert.assertEquals(5, entity.booksCount)
        Assert.assertEquals("name", entity.name)
    }

    @Test
    fun toSerie() {
        val local = SerieLocal().apply {
            id = 1
            name = "name"
        }

        val entity = local.toSerie()

        Assert.assertEquals(1L, entity.id)
        Assert.assertEquals("name", entity.name)
    }

    @Test
    fun toReader() {
        val local = ReaderLocal().apply {
            id = 1
            booksCount = 5
            name = "name"
        }

        val entity = local.toReader()

        Assert.assertEquals(1L, entity.id)
        Assert.assertEquals(5, entity.booksCount)
        Assert.assertEquals("name", entity.name)
    }

    @Test
    fun toTrack() {
        val local = TrackLocal().apply {
            id = 1
            time = 4
            url = "url"
        }

        val entity = local.toEntity()

        Assert.assertEquals(1L, entity.id)
        Assert.assertEquals(4L, entity.time)
        Assert.assertEquals("url", entity.url)
    }
}