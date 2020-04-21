package com.doneit.ascend.source.storage.remote.repository.chats.socket

import androidx.lifecycle.LiveData
import com.doneit.ascend.source.storage.remote.data.request.group.GroupSocketCookies
import com.doneit.ascend.source.storage.remote.data.response.chat.ChatSocketEventMessage

interface IChatSocketRepository {
    val messagesStream: LiveData<ChatSocketEventMessage>

    fun connect(cookies: GroupSocketCookies)

    fun disconnect()
}