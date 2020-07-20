package com.doneit.ascend.presentation.main.home.community_feed.share_post.common

import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import com.doneit.ascend.domain.entity.chats.ChatEntity
import com.doneit.ascend.presentation.main.chats.common.ChatDiffCallback

class ShareChatAdapter(
    private val onItemClick: (chat: ChatEntity) -> Unit
) : PagedListAdapter<ChatEntity, ShareChatViewHolder>(ChatDiffCallback()) {

    init {
        setHasStableIds(true)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShareChatViewHolder {
        return ShareChatViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: ShareChatViewHolder, position: Int) {
        holder.bind(getItem(position)!!, onItemClick)
    }

    override fun getItemId(position: Int): Long {
        return getItem(position)?.id!!
    }
}