package com.doneit.ascend.source.storage.remote.repository.community_feed.socket

import android.util.Log
import com.doneit.ascend.source.Constants
import com.doneit.ascend.source.storage.remote.data.request.group.CommunityFeedCookies
import com.doneit.ascend.source.storage.remote.data.response.community_feed.CommunityFeedEventMessage
import com.doneit.ascend.source.storage.remote.data.response.community_feed.CommunityFeedEventResponse
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.vrgsoft.networkmanager.livedata.SingleLiveEvent
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor

class CommunityFeedSocketRepository(
    private val gson: Gson
) : ICommunityFeedSocketRepository {
    private var socket: WebSocket? = null

    override val commentStream = SingleLiveEvent<CommunityFeedEventMessage?>()

    override fun connect(cookies: CommunityFeedCookies) {
        val builder = OkHttpClient.Builder()
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        builder.addInterceptor(loggingInterceptor)
        val client = builder.build()

        val request =
            Request.Builder().url(Constants.SOCKET_URL)
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
        commentStream.value = null
        socket?.close(1000, "")
    }

    private fun proceedMessage(message: String) {
        try {
            Log.d("SocketMessage", message)
            val result = gson.fromJson(message, CommunityFeedEventResponse::class.java)
            if(result.message != null) {
                commentStream.postValue(result.message)
            }
        } catch (exception: JsonSyntaxException) {
            exception.printStackTrace()
        }
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
                socket?.send(SUBSCRIBE_COMMUNITY_FEED_CHANNEL_COMMAND)
            }
        }
    }

    companion object {
        private const val SUBSCRIBE_COMMUNITY_FEED_CHANNEL_COMMAND =
            "{\"identifier\":\"{\\\"channel\\\":\\\"PostsChannel\\\"}\",\"command\": \"subscribe\"}"
        private const val COOKIE_KEY = "Cookie"
    }
}