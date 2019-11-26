package com.doneit.ascend.presentation.login.first_time_login.common.view_holder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.doneit.ascend.domain.entity.QuestionEntity
import com.doneit.ascend.presentation.login.R
import com.doneit.ascend.presentation.login.databinding.TemplateSelectAnswerItemBinding
import com.doneit.ascend.presentation.login.first_time_login.common.AnswerOptionsAdapter
import com.doneit.ascend.presentation.login.first_time_login.common.QuestionStateListener

class SelectAnswerViewHolder(
    private val binding: TemplateSelectAnswerItemBinding,
    private val listener: QuestionStateListener
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: QuestionEntity) {
        with(binding) {
            this.item = item

            item.options?.let {
                this.adapter = AnswerOptionsAdapter(it)
            }

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