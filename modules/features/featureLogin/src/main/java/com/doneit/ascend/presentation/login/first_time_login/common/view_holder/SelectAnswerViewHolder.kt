package com.doneit.ascend.presentation.login.first_time_login.common.view_holder

import android.view.LayoutInflater
import android.view.View
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
import com.doneit.ascend.presentation.login.first_time_login.common.RadioButtonClickListener
import kotlinx.android.synthetic.main.template_select_answer_item.view.*

class SelectAnswerViewHolder(
    private val binding: TemplateSelectAnswerItemBinding,
    private val listener: QuestionStateListener
) : RecyclerView.ViewHolder(binding.root) {

    private val clickListener = object : RadioButtonClickListener {
        override fun onClick(view: View, questionId: Long, optionId: Long) {
            listener.setState(questionId, true)
            listener.setSelectedAnswer(questionId, optionId)

            (binding.root.rgOptions as GridRadioGroup).clickByRadioButton(view)
        }
    }

    fun bind(item: QuestionEntity) {

        listener.setState(item.id, false)

        with(binding) {
            this.item = item

            item.options?.forEach {
                val binding =
                    TemplateAnswerItemBinding.inflate(LayoutInflater.from(binding.root.context))

                binding.item = it
                binding.rbOption.setOnCheckedChangeListener { compoundButton, b ->
                    clickListener.onClick(compoundButton, item.id, it.id)
                }

                val param: GridLayout.LayoutParams = GridLayout.LayoutParams(
                    GridLayout.spec(
                        GridLayout.UNDEFINED, GridLayout.FILL, 1f
                    ),
                    GridLayout.spec(GridLayout.UNDEFINED, GridLayout.FILL, 1f)
                )
                param.width = 0

                rgOptions.addView(binding.root, param)
            }

            rgOptions.selectedFirst()
            binding.executePendingBindings()
        }
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