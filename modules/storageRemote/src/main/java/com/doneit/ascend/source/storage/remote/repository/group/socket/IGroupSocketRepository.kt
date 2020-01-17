package com.doneit.ascend.source.storage.remote.repository.group.socket

import androidx.lifecycle.LiveData
import com.doneit.ascend.source.storage.remote.data.request.GroupSocketCookies
import com.doneit.ascend.source.storage.remote.data.response.SocketEventMessage
import com.doneit.ascend.source.storage.remote.data.response.SocketEventResponse

interface IGroupSocketRepository {
    val messagesStream: LiveData<SocketEventMessage>

    fun connect(cookies: GroupSocketCookies)

    fun sendMessage(message: String)

    fun disconnect()
}