package com.doneit.ascend.presentation.main.home.community_feed.share_post.common

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.doneit.ascend.domain.entity.chats.ChatEntity
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.databinding.TemplateShareChatItemBinding
import com.doneit.ascend.presentation.main.search.common.SearchViewHolder
import com.doneit.ascend.presentation.utils.extensions.createPlaceholderDrawable
import kotlinx.android.synthetic.main.template_my_chat_item.view.*

class ShareChatViewHolder(
    private val binding: TemplateShareChatItemBinding
) : SearchViewHolder(binding.root) {

    fun bind(
        item: ChatEntity,
        onClickListener: (chat: ChatEntity) -> Unit
    ) {
        binding.item = item
        itemView.isClickable = true
        loadChatImage(item)
        Glide.with(itemView.groupPlaceholder)
            .load(R.drawable.ic_group_placeholder)
            .circleCrop()
            .into(itemView.groupPlaceholder)
        itemView.setOnClickListener {
            onClickListener.invoke(item)
        }
    }

    private fun loadChatImage(item: ChatEntity) {
        if (item.title.isNotEmpty()) {
            Glide.with(itemView.chatImage)
                .load(item.image?.url)
                .placeholder(
                    itemView.chatImage.context.createPlaceholderDrawable(
                        item.title,
                        overrideSize = true
                    )
                )
                .circleCrop().into(itemView.chatImage)
        }
    }

    companion object {

        fun create(parent: ViewGroup): ShareChatViewHolder {
            val binding: TemplateShareChatItemBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.template_share_chat_item,
                parent,
                false
            )

            return ShareChatViewHolder(binding)
        }
    }
}