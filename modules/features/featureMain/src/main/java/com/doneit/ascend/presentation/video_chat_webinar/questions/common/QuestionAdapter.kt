package com.doneit.ascend.presentation.video_chat_webinar.questions.common

import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import com.doneit.ascend.domain.entity.webinar_question.WebinarQuestionEntity

class QuestionAdapter : PagedListAdapter<WebinarQuestionEntity, QuestionViewHolder>(QuestionDiffCallback()){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionViewHolder {
        return QuestionViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: QuestionViewHolder, position: Int) {
        val model =  getItem(position)
        model?.let { holder.bind(model) }
    }

}