package com.doneit.ascend.domain.gateway.gateway.notification

import com.nhaarman.mockitokotlin2.mock
import com.doneit.ascend.domain.gateway.common.base.LiveDataTest
import com.doneit.ascend.domain.gateway.gateway.NotificationGateway
import com.doneit.ascend.domain.use_case.common.NetworkManager
import com.doneit.ascend.domain.use_case.common.livedata.SingleLiveManager
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.ObsoleteCoroutinesApi
import org.junit.Assert.assertEquals
import org.junit.Test

@ExperimentalCoroutinesApi
@ObsoleteCoroutinesApi
internal class NotificationGatewayTest : LiveDataTest() {
    //region config

    inner class Config {
        var booleanType: MockNotificationShared.BooleanType = MockNotificationShared.BooleanType.NULL
        var intType: MockNotificationShared.IntType = MockNotificationShared.IntType.NULL

        fun setBooleanType(booleanType: MockNotificationShared.BooleanType) = this.apply {
            this.booleanType = booleanType
        }

        fun setIntType(intType: MockNotificationShared.IntType) = this.apply {
            this.intType = intType
        }

        fun build(): NotificationGateway {
            val errorsManager = mock<SingleLiveManager<com.doneit.ascend.domain.entity.errors.Error>> {
                on { call() } doAnswer {}
            }

            val manager = mock<NetworkManager> {
                on { errors } doReturn errorsManager
            }

            val prefs = MockNotificationShared(booleanType, intType)

            return NotificationGateway(manager, prefs)
        }
    }

    //endregion

    @Test
    fun getIsNeedShowDefault() {
        val gateway = Config()
            .setBooleanType(MockNotificationShared.BooleanType.NULL)
            .build()

        val result = gateway.isNeedShow()

        assertEquals(true, result)
    }

    @Test
    fun getNumLaunchDefault() {
        val gateway = Config()
            .setIntType(MockNotificationShared.IntType.NULL)
            .build()

        val result = gateway.getNumLaunches()

        assertEquals(0, result)
    }
}