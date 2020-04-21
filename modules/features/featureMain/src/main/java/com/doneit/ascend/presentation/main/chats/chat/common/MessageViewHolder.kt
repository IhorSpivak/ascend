package com.doneit.ascend.presentation.main.chats.chat.common

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.doneit.ascend.domain.entity.chats.MemberEntity
import com.doneit.ascend.domain.entity.chats.MessageEntity
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.databinding.ListItemMessageBinding

class MessageViewHolder(
    private val binding: ListItemMessageBinding
): RecyclerView.ViewHolder(binding.root) {

    fun bind(messageEntity: MessageEntity){
        binding.apply {
            //this.memberEntity = member
            this.messageEntity = messageEntity
        }
    }

    companion object {
        fun create(parent: ViewGroup): MessageViewHolder {
            val binding: ListItemMessageBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.list_item_message,
                parent,
                false
            )
            return MessageViewHolder(binding)
        }
    }
}