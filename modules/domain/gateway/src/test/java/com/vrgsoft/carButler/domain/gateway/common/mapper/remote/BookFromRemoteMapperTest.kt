package com.vrgsoft.carButler.domain.gateway.common.mapper.remote

import com.nhaarman.mockitokotlin2.mock
import org.junit.Assert.assertEquals
import org.junit.Test

class BookFromRemoteMapperTest {
    @Test
    fun toBook() {
        val serie: SerieResponse = mock {
            on { id } doReturn 1L
        }
        val author: AuthorResponse = mock {
            on { id } doReturn 1L
        }
        val genre: GenreResponse = mock {
            on { id } doReturn 1L
        }
        val reader: ReaderResponse = mock {
            on { id } doReturn 1L
        }
        val track: TrackResponse = mock {}

        val remote = BookResponse(
            id = 1,
            title = "title",
            description = "desc",
            image = "image",
            serie = serie,
            authors = listOf(author),
            genres = listOf(genre),
            rating = 5,
            readers = listOf(reader),
            tracks = listOf(track),
            totalTime = 1488
        )

        val entity = remote.toBook()
        val tracks = entity.tracks

        assertEquals(1, entity.id)
        assertEquals("title", entity.title)
        assertEquals("desc", entity.description)
        assertEquals("image", entity.imageUrl)
        assertEquals(1L, entity.author.id)
        assertEquals(1L, entity.reader.id)
        assertEquals(1488L, entity.totalTime)

        assertEquals(1, tracks.size)
    }

    @Test
    fun toEmptyBook() {
        val remote = BookResponse()

        val entity = remote.toBook()
        val tracks = entity.tracks

        assertEquals(-1L, entity.id)
        assertEquals("", entity.title)
        assertEquals("", entity.description)
        assertEquals("", entity.imageUrl)
        assertEquals(-1L, entity.author.id)
        assertEquals(-1L, entity.reader.id)
        assertEquals(-1L, entity.totalTime)

        assertEquals(true, tracks.isEmpty())
    }

    @Test
    fun toTrack() {
        val remote = TrackResponse(
            time = 1488,
            url = "url"
        )

        val track = remote.toEntity()

        assertEquals(1488, track.time)
        assertEquals("url", track.url)
    }

    @Test
    fun toAuthor() {
        val remote = AuthorResponse(
            id = 1,
            name = "name",
            booksCount = 15
        )

        val author = remote.toAuthor()

        assertEquals(1L, author.id)
        assertEquals("name", author.name)
        assertEquals(15, author.booksCount)
    }

    @Test
    fun toEmptyAuthor() {
        val remote = AuthorResponse()

        val author = remote.toAuthor()

        assertEquals(-1L, author.id)
        assertEquals("", author.name)
        assertEquals(0, author.booksCount)
    }

    @Test
    fun toReader() {
        val remote = ReaderResponse(
            id = 1,
            name = "name",
            booksCount = 15
        )

        val reader = remote.toReader()

        assertEquals(1L, reader.id)
        assertEquals("name", reader.name)
        assertEquals(15, reader.booksCount)
    }

    @Test
    fun toEmptyReader() {
        val remote = ReaderResponse()

        val author = remote.toReader()

        assertEquals(-1L, author.id)
        assertEquals("", author.name)
        assertEquals(0, author.booksCount)
    }

    @Test
    fun toSerie() {
        val remote = SerieResponse(
            id = 1,
            name = "name"
        )

        val serie = remote.toSerie()

        assertEquals(1L, serie.id)
        assertEquals("name", serie.name)
    }

    @Test
    fun toEmptySerie() {
        val remote = SerieResponse()

        val serie = remote.toSerie()

        assertEquals(-1L, serie.id)
        assertEquals("", serie.name)
    }
}