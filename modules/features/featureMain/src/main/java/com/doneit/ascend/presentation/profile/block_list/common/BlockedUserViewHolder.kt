package com.doneit.ascend.presentation.profile.block_list.common

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.doneit.ascend.domain.entity.chats.BlockedUserEntity
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.databinding.ListItemChatMemberReturnBinding

class BlockedUserViewHolder (
    private val binding: ListItemChatMemberReturnBinding,
    private val onReturn: (member: BlockedUserEntity) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(member: BlockedUserEntity){
        binding.apply {
            user = member
            delete.setOnClickListener {
                onReturn.invoke(member)
            }
        }
    }

    companion object {
        fun create(
            parent: ViewGroup,
            onRemove: (member: BlockedUserEntity) -> Unit
        ): BlockedUserViewHolder {
            val binding: ListItemChatMemberReturnBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.list_item_chat_member_return,
                parent,
                false
            )
            return BlockedUserViewHolder(
                binding,
                onRemove
            )
        }
    }
}