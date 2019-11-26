package com.doneit.ascend.presentation.login.first_time_login.common.view_holder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.doneit.ascend.domain.entity.QuestionEntity
import com.doneit.ascend.presentation.login.R
import com.doneit.ascend.presentation.login.databinding.TemplateQuestionItemBinding
import com.doneit.ascend.presentation.login.first_time_login.common.QuestionStateListener
import com.doneit.ascend.presentation.login.models.PresentationAnswerModel
import com.doneit.ascend.presentation.login.models.ValidationResult
import com.doneit.ascend.presentation.login.utils.isValidAnswer

class QuestionViewHolder(
    private val binding: TemplateQuestionItemBinding,
    private val listener: QuestionStateListener
) : RecyclerView.ViewHolder(binding.root) {

    private lateinit var model: PresentationAnswerModel

    fun bind(item: QuestionEntity) {

        model = PresentationAnswerModel(item.id, -1)
        initValidator()

        with(binding) {
            this.item = item
            this.answerModel = model
            binding.executePendingBindings()
        }
    }

    private fun initValidator() {
        model.answer.validator = { s ->
            val result = ValidationResult()

            if (s.isValidAnswer().not()) {
                result.isSussed = false
                result.errors.add(R.string.answer_error)
            }

            result
        }
    }

    companion object {
        fun create(parent: ViewGroup, listener: QuestionStateListener): QuestionViewHolder {
            val binding: TemplateQuestionItemBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.template_question_item,
                parent,
                false
            )

            return QuestionViewHolder(binding,listener)
        }
    }
}