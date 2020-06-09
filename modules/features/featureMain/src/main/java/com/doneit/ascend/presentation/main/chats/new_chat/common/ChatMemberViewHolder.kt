package com.doneit.ascend.presentation.main.chats.new_chat.common

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.doneit.ascend.domain.entity.chats.MemberEntity
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.databinding.ListItemChatMemberBinding

class ChatMemberViewHolder(
    private val binding: ListItemChatMemberBinding,
    private val onRemove: (member: MemberEntity) -> Unit,
    private val onBlock: (member: MemberEntity) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(member: MemberEntity, isUserMasterMind: Boolean = false) {
        binding.apply {
            user = member
            this.isUserMasterMind = isUserMasterMind
            delete.setOnClickListener {
                onRemove(member)
            }
            btnBlock.setOnClickListener {
                onBlock(member)
                btnBlock.isEnabled = false
            }
        }
    }

    companion object {
        fun create(
            parent: ViewGroup,
            onRemove: (member: MemberEntity) -> Unit,
            onBlock: (member: MemberEntity) -> Unit
        ): ChatMemberViewHolder {
            val binding: ListItemChatMemberBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.list_item_chat_member,
                parent,
                false
            )
            return ChatMemberViewHolder(binding, onRemove, onBlock)
        }
    }
}