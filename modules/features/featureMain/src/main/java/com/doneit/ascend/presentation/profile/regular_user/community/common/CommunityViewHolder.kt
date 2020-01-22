package com.doneit.ascend.presentation.profile.regular_user.community.common

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.databinding.TemplateAnswerItemBinding
import com.doneit.ascend.presentation.models.PresentationCommunityModel

class CommunityViewHolder(
    private val binding: TemplateAnswerItemBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: PresentationCommunityModel, onSelection: () -> Unit) {
        binding.title = item.title
        binding.rbOption.setOnCheckedChangeListener { compoundButton, b ->
            if (b) {
                onSelection.invoke()
            }
        }
        binding.rbOption.isChecked = item.isSelected
    }

    companion object {
        fun create(parent: ViewGroup): CommunityViewHolder {
            val binding: TemplateAnswerItemBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.template_answer_item,
                parent,
                false
            )

            return CommunityViewHolder(binding)
        }
    }
}
