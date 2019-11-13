package com.vrgsoft.carButler.domain.gateway.gateway.genre

import com.nhaarman.mockitokotlin2.mock
import com.vrgsoft.carButler.domain.entity.errors.Error
import com.vrgsoft.carButler.domain.gateway.common.base.LiveDataTest
import com.vrgsoft.carButler.domain.gateway.gateway.GenreGateway
import com.vrgsoft.carButler.domain.use_case.common.NetworkManager
import com.vrgsoft.carButler.domain.use_case.common.livedata.SingleLiveManager
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.ObsoleteCoroutinesApi
import org.junit.Assert
import org.junit.Test
import com.vrgsoft.carButler.source.storage.local.repository.genre.IGenreRepository as LocalGenreRepository
import com.vrgsoft.carButler.source.storage.remote.repository.genre.IGenreRepository as RemoteGenreRepository


@ExperimentalCoroutinesApi
@ObsoleteCoroutinesApi
internal class GenreGatewayTest : LiveDataTest() {

    //region config

    inner class Config {
        var remoteResult: MockGenreRemote.Type = MockGenreRemote.Type.EMPTY
        var localResult: MockGenreLocal.Type = MockGenreLocal.Type.EMPTY

        fun setRemoteResult(result: MockGenreRemote.Type) = this.apply {
            remoteResult = result
        }

        fun setLocalResult(result: MockGenreLocal.Type) = this.apply {
            localResult = result
        }

        fun build(): GenreGateway {
            val errorsManager = mock<SingleLiveManager<Error>> {
                on { call() } doAnswer {}
            }

            val manager = mock<NetworkManager> {
                on { errors } doReturn errorsManager
            }

            val remote = MockGenreRemote(remoteResult)
            val local = MockGenreLocal(localResult)

            return GenreGateway(manager, remote, local)
        }
    }

    //endregion

    @Test
    fun getEmptyGenreList() {
        val gateway = Config()
            .setRemoteResult(MockGenreRemote.Type.EMPTY)
            .setLocalResult(MockGenreLocal.Type.EMPTY)
            .build()

        gateway.getGenreList().assert {
            Assert.assertEquals(true, it.isEmpty())
        }
    }

    @Test
    fun getOnlyLocalGenreList() {
        val gateway = Config()
            .setRemoteResult(MockGenreRemote.Type.EMPTY)
            .setLocalResult(MockGenreLocal.Type.MULTI)
            .build()

        gateway.getGenreList().assert {
            Assert.assertEquals(3, it.size)
        }
    }

    @Test
    fun getOnlyRemoteGenreList() {
        val gateway = Config()
            .setRemoteResult(MockGenreRemote.Type.MULTI)
            .setLocalResult(MockGenreLocal.Type.EMPTY)
            .build()

        gateway.getGenreList().assert {
            Assert.assertEquals(3, it.size)
        }
    }

    @Test
    fun getLocalAndRemoteGenreTest() {
        val gateway = Config()
            .setRemoteResult(MockGenreRemote.Type.MULTI)
            .setLocalResult(MockGenreLocal.Type.MULTI)
            .build()

        gateway.getGenreList().assert {
            Assert.assertEquals(6, it.size)
        }
    }

    @Test
    fun getLocalAndErrorGenreTest() {
        val gateway = Config()
            .setRemoteResult(MockGenreRemote.Type.ERROR)
            .setLocalResult(MockGenreLocal.Type.MULTI)
            .build()

        gateway.getGenreList().assert(1) {
            Assert.assertEquals(3, it.size)
        }
    }
}