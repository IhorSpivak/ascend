package com.doneit.ascend.presentation.video_chat_webinar.in_progress.common

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.doneit.ascend.domain.entity.webinar_question.WebinarQuestionEntity
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.base.LifecycleViewHolder
import com.doneit.ascend.presentation.main.databinding.TemplateWebinarQuestionTransparentItemBinding

class FourQuestionViewHolder(
    private val binding: TemplateWebinarQuestionTransparentItemBinding
) : LifecycleViewHolder(binding.root) {

    fun bind(item: WebinarQuestionEntity) {
        binding.item = item
    }

    companion object {
        fun create(parent: ViewGroup): FourQuestionViewHolder {
            val binding: TemplateWebinarQuestionTransparentItemBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.template_webinar_question_transparent_item,
                parent,
                false
            )

            return FourQuestionViewHolder(binding)
        }
    }
}