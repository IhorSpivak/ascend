package com.doneit.ascend.domain.gateway.common.base_gateway

import com.nhaarman.mockitokotlin2.spy
import com.nhaarman.mockitokotlin2.verify
import com.doneit.ascend.domain.gateway.common.BaseGateway
import com.doneit.ascend.domain.gateway.common.base.LiveDataTest
import com.doneit.ascend.domain.use_case.common.NetworkManager
import com.doneit.ascend.source.storage.remote.common.error.ConnectionError
import com.doneit.ascend.source.storage.remote.common.result.BaseResult
import com.doneit.ascend.source.storage.remote.common.result.ErrorResult
import com.doneit.ascend.source.storage.remote.common.result.SuccessResult
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert
import org.junit.Test

@ObsoleteCoroutinesApi
@ExperimentalCoroutinesApi
class BaseGatewayTest : LiveDataTest() {

    private lateinit var gateway: BaseGateway
    private lateinit var manager: NetworkManager

    override fun setUpDetail() {
        manager = spy(NetworkManager())
        gateway = MockedGateway(manager)
    }

    @Test
    fun testSuccess() {
        val testData = 1488
        runBlockingTest {
            val call: (suspend () -> BaseResult<Int>) = {
                SuccessResult(testData)
            }
            val result = gateway.executeRemote(call)

            verify(manager).startProcessing()
            verify(manager).stopProcessing()
            Assert.assertEquals(true, result is SuccessResult)
            Assert.assertEquals(testData, (result as SuccessResult).data)
        }
    }

    @Test
    fun testError() {
        runBlockingTest {
            val call: (suspend () -> BaseResult<Int>) = {
                ErrorResult(ConnectionError())
            }
            val result = gateway.executeRemote(call)

            verify(manager).startProcessing()
            verify(manager).stopProcessing()
            Assert.assertEquals(true, result is ErrorResult)
            Assert.assertEquals(true, (result as ErrorResult).error is ConnectionError)
        }
    }
}