package com.doneit.ascend.presentation.video_chat.common

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.doneit.ascend.domain.entity.SocketUserEntity
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.databinding.TemplateChatParticipantBinding

class ChatParticipantViewHolder(
    private val binding: TemplateChatParticipantBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(entity: SocketUserEntity) {
        binding.item = entity
    }

    companion object {
        fun create(parent: ViewGroup): ChatParticipantViewHolder {
            val binding: TemplateChatParticipantBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.template_chat_participant,
                parent,
                false
            )

            return ChatParticipantViewHolder(binding)
        }
    }
}