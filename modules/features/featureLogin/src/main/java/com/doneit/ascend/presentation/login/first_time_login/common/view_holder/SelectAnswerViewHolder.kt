package com.doneit.ascend.presentation.login.first_time_login.common.view_holder

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.GridLayout
import androidx.databinding.DataBindingUtil
import com.doneit.ascend.domain.entity.QuestionListEntity
import com.doneit.ascend.presentation.login.R
import com.doneit.ascend.presentation.login.databinding.TemplateAnswerItemBinding
import com.doneit.ascend.presentation.login.databinding.TemplateSelectAnswerItemBinding
import com.doneit.ascend.presentation.login.first_time_login.common.GridRadioGroup
import com.doneit.ascend.presentation.login.first_time_login.common.QuestionStateListener
import com.doneit.ascend.presentation.main.base.LifecycleViewHolder

class SelectAnswerViewHolder(
    private val binding: TemplateSelectAnswerItemBinding,
    private val listener: QuestionStateListener
) : LifecycleViewHolder(binding.root) {

    fun bind(item: QuestionListEntity) {
        with(binding) {
            this.title = item.community?.title

            item.community?.answerOptions?.forEach { optionValue ->
                val binding =
                    TemplateAnswerItemBinding.inflate(LayoutInflater.from(binding.root.context))

                binding.title = optionValue
                binding.rbOption.setOnCheckedChangeListener { view, isChecked ->
                    if (isChecked) {
                        (rgOptions as GridRadioGroup).clickByRadioButton(view)

                        listener.setCommunity(optionValue)
                    }
                }

                rgOptions.addView(binding.root, getOptionViewParams())
            }

            binding.executePendingBindings()
        }
    }

    private fun getOptionViewParams(): GridLayout.LayoutParams {
        val param = GridLayout.LayoutParams(
            GridLayout.spec(
                GridLayout.UNDEFINED, GridLayout.FILL, 1f
            ),
            GridLayout.spec(GridLayout.UNDEFINED, GridLayout.FILL, 1f)
        )
        param.width = 0

        return param
    }

    companion object {
        fun create(parent: ViewGroup, listener: QuestionStateListener): SelectAnswerViewHolder {
            val binding: TemplateSelectAnswerItemBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.template_select_answer_item,
                parent,
                false
            )

            return SelectAnswerViewHolder(binding, listener)
        }
    }
}