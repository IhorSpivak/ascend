package com.doneit.ascend.source.storage.remote.repository.chats.socket

import android.util.Log
import androidx.lifecycle.LiveData
import com.doneit.ascend.source.storage.remote.data.request.group.GroupSocketCookies
import com.doneit.ascend.source.storage.remote.data.response.chat.ChatSocketEventMessage
import com.doneit.ascend.source.storage.remote.data.response.chat.ChatSocketEventResponse
import com.doneit.ascend.source.storage.remote.data.response.group.SocketEventMessage
import com.doneit.ascend.source.storage.remote.data.response.group.SocketEventResponse
import com.doneit.ascend.source.storage.remote.repository.group.socket.GroupSocketRepository
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.vrgsoft.networkmanager.livedata.SingleLiveEvent
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor

class ChatSocketRepository(
    private val gson: Gson
) : IChatSocketRepository {
    private var socket: WebSocket? = null

    override val messagesStream = SingleLiveEvent<ChatSocketEventMessage>()

    override fun connect(cookies: GroupSocketCookies) {
        val builder = OkHttpClient.Builder()
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        builder.addInterceptor(loggingInterceptor)
        val client = builder.build()

        val request =
            Request.Builder().url(URL)
                .addHeader(COOKIE_KEY, cookies.toString())//required for authorization
                .addHeader(
                    "Origin",
                    ""
                )//Server accepts any origin, but presence of this header is required!!!
                .build()

        socket = client.newWebSocket(request, getWebSocketListener())
        client.dispatcher.executorService.shutdown()
    }

    override fun disconnect() {
        socket?.close(1000, "")
    }

    private fun getWebSocketListener(): WebSocketListener {
        return object : WebSocketListener() {
            override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                super.onClosed(webSocket, code, reason)
            }

            override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
                super.onClosing(webSocket, code, reason)
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                super.onFailure(webSocket, t, response)
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                //optimization in order to avoid ping messages processing
                if(text.contains("ping").not()) {
                    proceedMessage(text)
                }
            }

            override fun onOpen(webSocket: WebSocket, response: Response) {
                socket?.send(SUBSCRIBE_GROUP_CHANNEL_COMMAND)
            }
        }
    }

    private fun proceedMessage(message: String) {
        try {
            Log.d("SocketMessage", message)
            val result = gson.fromJson(message, ChatSocketEventResponse::class.java)
            if(result.message != null) {
                messagesStream.postValue(result.message)
            }
        } catch (exception: JsonSyntaxException) {
            exception.printStackTrace()
        }
    }

    companion object {
        private const val SUBSCRIBE_GROUP_CHANNEL_COMMAND =
            "{\"identifier\":\"{\\\"channel\\\":\\\"ChatChannel\\\"}\",\"command\": \"subscribe\"}"
        private const val URL = "wss://ascend-backend.herokuapp.com/cable"
        //private const val URL = "wss://ascend-backend-prod.herokuapp.com/cable"
        private const val COOKIE_KEY = "Cookie"
    }
}