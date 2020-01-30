package com.doneit.ascend.source.storage.remote.repository.group.socket

import androidx.lifecycle.LiveData
import com.doneit.ascend.source.storage.remote.data.request.group.GroupSocketCookies
import com.doneit.ascend.source.storage.remote.data.response.group.SocketEventMessage

interface IGroupSocketRepository {
    val messagesStream: LiveData<SocketEventMessage>

    fun connect(cookies: GroupSocketCookies)

    fun sendMessage(message: String)

    fun disconnect()
}