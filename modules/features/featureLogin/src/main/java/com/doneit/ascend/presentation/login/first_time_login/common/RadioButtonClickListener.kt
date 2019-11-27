package com.doneit.ascend.presentation.login.first_time_login.common

import android.view.View

interface RadioButtonClickListener {
    fun onClick(view: View, questionId: Long, optionId: Long)
}