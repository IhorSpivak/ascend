package com.doneit.ascend.presentation.main.chats.common

import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import com.doneit.ascend.domain.entity.chats.ChatEntity


class MyChatsAdapter(
    private val onItemClick: (id: Long) -> Unit,
    private val onDeleteListener: (id: Long) -> Unit
) : PagedListAdapter<ChatEntity, MyChatViewHolder>(ChatDiffCallback()) {

    init {
        setHasStableIds(true)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyChatViewHolder {
        return MyChatViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: MyChatViewHolder, position: Int) {
        holder.bind(getItem(position)!!, onDeleteListener, onItemClick)
    }

    override fun getItemId(position: Int): Long {
        return getItem(position)?.id!!
    }
}
