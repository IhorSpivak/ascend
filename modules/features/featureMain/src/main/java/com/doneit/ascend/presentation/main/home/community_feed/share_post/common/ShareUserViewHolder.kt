package com.doneit.ascend.presentation.main.home.community_feed.share_post.common

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.doneit.ascend.domain.entity.AttendeeEntity
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.databinding.TemplateShareUserItemBinding
import com.doneit.ascend.presentation.main.search.common.SearchViewHolder


class ShareUserViewHolder(
    private val binding: TemplateShareUserItemBinding
) : SearchViewHolder(binding.root) {

    fun bind(
        item: AttendeeEntity,
        onClickListener: (id: Long) -> Unit
    ) {
        binding.user = item
        itemView.isClickable = true
        itemView.setOnClickListener {
            onClickListener.invoke(item.id)
        }
    }

    companion object {

        fun create(parent: ViewGroup): ShareUserViewHolder {
            val binding: TemplateShareUserItemBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.template_share_user_item,
                parent,
                false
            )

            return ShareUserViewHolder(binding)
        }
    }
}