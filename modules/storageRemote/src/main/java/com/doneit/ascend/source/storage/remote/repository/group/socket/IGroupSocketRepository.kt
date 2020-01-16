package com.doneit.ascend.source.storage.remote.repository.group.socket

import com.doneit.ascend.source.storage.remote.data.request.GroupSocketCookies
import okhttp3.WebSocketListener

interface IGroupSocketRepository {
    fun connect(listener: WebSocketListener, cookies: GroupSocketCookies)

    fun sendMessage(message: String)

    fun disconnect()
}