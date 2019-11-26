package com.doneit.ascend.presentation.login.first_time_login.common.view_holder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.doneit.ascend.domain.entity.AnswerOptionEntity
import com.doneit.ascend.presentation.login.R
import com.doneit.ascend.presentation.login.databinding.TemplateAnswerItemBinding

class AnswerViewHolder(
    private val binding: TemplateAnswerItemBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: AnswerOptionEntity) {
        with(binding) {
            this.item = item
            binding.executePendingBindings()
        }
    }

    companion object {
        fun create(parent: ViewGroup): AnswerViewHolder {
            val binding: TemplateAnswerItemBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.template_answer_item,
                parent,
                false
            )

            return AnswerViewHolder(binding)
        }
    }
}