package com.doneit.ascend.presentation.login.first_time_login.common

interface QuestionStateListener {
    fun setState(questionId: Long, isValid: Boolean)
    fun setQuestionAnswer(questionId: Long, answer: String)
    fun setSelectedAnswer(questionId: Long, optionId: Long)
    fun setCommunity(community: String)
}