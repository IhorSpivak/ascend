package com.doneit.ascend.presentation.video_chat_webinar.questions.common

import androidx.recyclerview.widget.DiffUtil
import com.doneit.ascend.domain.entity.webinar_question.WebinarQuestionEntity

class QuestionDiffCallback : DiffUtil.ItemCallback<WebinarQuestionEntity>() {
    override fun areContentsTheSame(oldItem: WebinarQuestionEntity, newItem: WebinarQuestionEntity): Boolean {
        return oldItem == newItem
    }

    override fun areItemsTheSame(oldItem: WebinarQuestionEntity, newItem: WebinarQuestionEntity): Boolean {
        return (oldItem.id == newItem.id)
    }
}