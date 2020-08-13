package com.doneit.ascend.presentation.video_chat.attachments.common

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.doneit.ascend.domain.entity.AttachmentEntity
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.databinding.ListItemAttachmentLinkBinding
import com.doneit.ascend.presentation.utils.extensions.toAttachmentDate

class AttachmentLinkViewHolder(
    private val binding: ListItemAttachmentLinkBinding
) : AttachmentViewHolder(binding.root) {

    fun bind(
        item: AttachmentEntity,
        onCopyClick: (AttachmentEntity) -> Unit,
        onDeleteListener: (id: Long) -> Unit
    ) {
        binding.item = item
        binding.copy.setOnClickListener {
            onCopyClick.invoke(item)
        }
        binding.date = item.createdAt.toAttachmentDate()
        binding.ibDelete.setOnClickListener {
            onDeleteListener.invoke(item.id)
        }
        binding.executePendingBindings()
    }

    companion object {
        fun create(
            parent: ViewGroup
        ): AttachmentLinkViewHolder {
            val binding: ListItemAttachmentLinkBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.list_item_attachment_link,
                parent,
                false
            )

            return AttachmentLinkViewHolder(binding)
        }
    }
}