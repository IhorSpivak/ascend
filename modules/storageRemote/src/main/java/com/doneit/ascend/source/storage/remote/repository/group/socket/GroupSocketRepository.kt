package com.doneit.ascend.source.storage.remote.repository.group.socket

import com.doneit.ascend.source.storage.remote.data.request.GroupSocketCookies
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okhttp3.logging.HttpLoggingInterceptor

class GroupSocketRepository : IGroupSocketRepository {

    private var socket: WebSocket? = null

    override fun connect(listener: WebSocketListener, cookies: GroupSocketCookies) {
        val builder = OkHttpClient.Builder()
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        builder.addInterceptor(loggingInterceptor)
        val client = builder.build()

        val request =
            Request.Builder().url(URL)
                .addHeader(COOKIE_KEY,cookies.toString())//required for authorization
                .addHeader("Origin","")//Server accepts any origin, but presence of this header is required!!!
                .build()

        socket = client.newWebSocket(request, listener)
        client.dispatcher.executorService.shutdown()
    }

    override fun sendMessage(message: String) {
        socket?.send(message)
    }

    override fun disconnect() {
        socket?.close(1000,"")
    }

    companion object {
        private const val URL = "wss://ascend-backend.herokuapp.com/cable"
        private const val COOKIE_KEY = "Cookie"
    }
}