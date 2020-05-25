package com.doneit.ascend.presentation.video_chat_webinar.questions.common

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.doneit.ascend.domain.entity.webinar_question.WebinarQuestionEntity
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.base.LifecycleViewHolder
import com.doneit.ascend.presentation.main.databinding.TemplateWebinarQuestionItemBinding
import com.doneit.ascend.presentation.utils.extensions.toChatDate

class QuestionViewHolder(
    private val binding: TemplateWebinarQuestionItemBinding
) : LifecycleViewHolder(binding.root) {

    fun bind(item: WebinarQuestionEntity) {
        binding.item = item
        binding.tvDate.text = item.createdAt?.toChatDate(binding.root.context)
    }

    companion object {
        fun create(parent: ViewGroup): QuestionViewHolder {
            val binding: TemplateWebinarQuestionItemBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.template_webinar_question_item,
                parent,
                false
            )

            return QuestionViewHolder(binding)
        }
    }
}