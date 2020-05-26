package com.doneit.ascend.source.storage.remote.repository.group.questions_socket

import androidx.lifecycle.LiveData
import com.doneit.ascend.source.storage.remote.data.request.group.GroupSocketCookies
import com.doneit.ascend.source.storage.remote.data.response.group.QuestionSocketEventMessage

interface IQuestionSocketRepository {
    val questionStream: LiveData<QuestionSocketEventMessage?>

    fun connect(cookies: GroupSocketCookies)

    fun sendQuestion(question: String)

    fun disconnect()
}