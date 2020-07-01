package com.doneit.ascend.presentation.video_chat_webinar.in_progress.common

import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import com.doneit.ascend.domain.entity.webinar_question.WebinarQuestionEntity
import com.doneit.ascend.presentation.video_chat_webinar.questions.common.QuestionDiffCallback

class FourQuestionsAdapter : PagedListAdapter<WebinarQuestionEntity, FourQuestionViewHolder>(
    QuestionDiffCallback()
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FourQuestionViewHolder {
        return FourQuestionViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: FourQuestionViewHolder, position: Int) {
        val model = getItem(position)
        model?.let { holder.bind(model) }
    }

}