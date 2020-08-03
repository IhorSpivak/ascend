package com.doneit.ascend.presentation.main.chats.common

import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import com.doneit.ascend.domain.entity.chats.ChatEntity
import com.doneit.ascend.domain.entity.user.UserEntity


class MyChatsAdapter(
    private val onItemClick: (chat: ChatEntity) -> Unit,
    private val onDeleteListener: (chat: ChatEntity) -> Unit
) : PagedListAdapter<ChatEntity, MyChatViewHolder>(ChatDiffCallback()) {

    private var user: UserEntity? = null

    init {
        setHasStableIds(true)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyChatViewHolder {
        return MyChatViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: MyChatViewHolder, position: Int) {
        holder.bind(getItem(position)!!, onDeleteListener, onItemClick, user!!)
    }

    override fun getItemId(position: Int): Long {
        return getItem(position)?.id!!
    }

    fun updateUser(user: UserEntity) {
        if (this.user == null) {
            this.user = user
            notifyItemRangeChanged(0, itemCount - 1)
        }
    }
}
