package com.doneit.ascend.presentation.login.first_time_login.common.view_holder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.doneit.ascend.domain.entity.QuestionEntity
import com.doneit.ascend.presentation.login.R
import com.doneit.ascend.presentation.login.databinding.TemplateQuestionItemBinding
import com.doneit.ascend.presentation.login.first_time_login.common.QuestionStateListener
import com.doneit.ascend.presentation.login.models.PresentationAnswerModel
import com.doneit.ascend.presentation.login.models.ValidationResult
import com.doneit.ascend.presentation.login.utils.isValidAnswer
import com.doneit.ascend.presentation.main.base.LifecycleViewHolder

class QuestionViewHolder(
    private val binding: TemplateQuestionItemBinding,
    private val listener: QuestionStateListener
) : LifecycleViewHolder(binding.root) {

    private var model: PresentationAnswerModel = PresentationAnswerModel(1, -1)

    fun bind(item: QuestionEntity) {
        listener.setState(item.id, false)

        model = PresentationAnswerModel(item.id, -1)

        with(binding) {
            this.item = item
            this.answerModel = model
            this.lifecycleOwner = this@QuestionViewHolder
            this@QuestionViewHolder.onAppear(model.answer.observableError)
            binding.executePendingBindings()

            initValidator(item.id)
        }
    }

    private fun initValidator(id: Long) {
        model.answer.validator = { s ->
            val result = ValidationResult()

            if (s.isValidAnswer().not()) {
                result.isSussed = false
                result.errors.add(R.string.answer_error)
            }

            listener.setState(id, result.isSussed)
            listener.setQuestionAnswer(id, s)

            model.answer.onFieldInvalidate = {
                itemView.requestLayout()
                itemView.invalidate()
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

            return QuestionViewHolder(binding, listener)
        }
    }
}