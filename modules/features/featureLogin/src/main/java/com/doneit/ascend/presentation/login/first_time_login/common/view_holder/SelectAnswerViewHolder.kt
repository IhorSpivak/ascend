package com.doneit.ascend.presentation.login.first_time_login.common.view_holder

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.GridLayout
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.doneit.ascend.domain.entity.QuestionEntity
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

    fun bind(item: QuestionEntity) {
        listener.setState(item.id, false)

        with(binding) {
            this.item = item

            item.options?.forEach { optionIt ->
                val binding =
                    TemplateAnswerItemBinding.inflate(LayoutInflater.from(binding.root.context))

                binding.item = optionIt
                binding.rbOption.setOnCheckedChangeListener { view, isChecked ->
                    if (isChecked) {
                        (rgOptions as GridRadioGroup).clickByRadioButton(view)

                        listener.setState(item.id, true)
                        listener.setSelectedAnswer(item.id, optionIt.id)
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