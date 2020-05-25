package com.doneit.ascend.source.storage.remote.repository.group.questions_socket

import android.util.Log
import com.doneit.ascend.source.Constants.SOCKET_URL
import com.doneit.ascend.source.storage.remote.data.request.group.GroupSocketCookies
import com.doneit.ascend.source.storage.remote.data.response.group.QuestionSocketEventMessage
import com.doneit.ascend.source.storage.remote.data.response.group.QuestionSocketEventResponse
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.vrgsoft.networkmanager.livedata.SingleLiveEvent
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor

class QuestionSocketRepository(
    private val gson: Gson
) : IQuestionSocketRepository {
    override val questionStream = SingleLiveEvent<QuestionSocketEventMessage>()

    private var socket: WebSocket? = null

    override fun connect(cookies: GroupSocketCookies) {
        val builder = OkHttpClient.Builder()
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        builder.addInterceptor(loggingInterceptor)
        val client = builder.build()

        val request =
            Request.Builder().url(SOCKET_URL)
                .addHeader(COOKIE_KEY, cookies.toString())//required for authorization
                .addHeader(
                    "Origin",
                    ""
                )//Server accepts any origin, but presence of this header is required!!!
                .build()

        socket = client.newWebSocket(request, getWebSocketListener())
        client.dispatcher.executorService.shutdown()
    }

    override fun sendQuestion(question: String) {
    }

    override fun disconnect() {
        questionStream.value = null
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
                socket?.send(SUBSCRIBE_QUESTION_CHANNEL_COMMAND)
            }
        }
    }

    private fun proceedMessage(message: String) {
        try {
            Log.d("SocketMessage", message)
            val result = gson.fromJson(message, QuestionSocketEventResponse::class.java)
            if(result.message?.event != null) { //todo replace by Gson deserializer with exception on missing fields
                questionStream.postValue(result.message)
            }
        } catch (exception: JsonSyntaxException) {
            exception.printStackTrace()
        }
    }

    companion object {
        private const val SUBSCRIBE_QUESTION_CHANNEL_COMMAND =
            "{\"identifier\":\"{\\\"channel\\\":\\\"QuestionsChannel\\\"}\",\"command\": \"subscribe\"}"
        private const val COOKIE_KEY = "Cookie"
    }

}