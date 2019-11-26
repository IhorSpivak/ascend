package com.doneit.ascend.presentation.login.first_time_login.common

interface QuestionStateListener {
    fun setState(questionId: Long, isValid: Boolean)
}