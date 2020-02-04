package com.doneit.ascend.presentation.video_chat.attachments.common

import androidx.recyclerview.widget.DiffUtil
import com.doneit.ascend.domain.entity.AttachmentEntity

class AttachmentDiffCallback : DiffUtil.ItemCallback<AttachmentEntity>() {
    override fun areContentsTheSame(oldItem: AttachmentEntity, newItem: AttachmentEntity): Boolean {
        return oldItem == newItem
    }

    override fun areItemsTheSame(oldItem: AttachmentEntity, newItem: AttachmentEntity): Boolean {
        return (oldItem.id == newItem.id)
    }
}