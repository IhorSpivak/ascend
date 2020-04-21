package com.doneit.ascend.presentation.main.chats.new_chat.common

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.doneit.ascend.domain.entity.AttendeeEntity
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.databinding.ListItemChatMemberBinding

class ChatMemberViewHolder(
    private val binding: ListItemChatMemberBinding,
    private val onRemove: (member: AttendeeEntity) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(member: AttendeeEntity){
        binding.apply {
            user = member
            delete.setOnClickListener {
                onRemove.invoke(member)
            }
        }
    }

    companion object {
        fun create(
            parent: ViewGroup,
            onRemove: (member: AttendeeEntity) -> Unit
        ): ChatMemberViewHolder {
            val binding: ListItemChatMemberBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.list_item_chat_member,
                parent,
                false
            )
            return ChatMemberViewHolder(binding, onRemove)
        }
    }
}