package com.doneit.ascend.presentation.video_chat.attachments.common

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.doneit.ascend.domain.entity.AttachmentEntity
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.databinding.ListItemAttachmentImageBinding
import com.doneit.ascend.presentation.utils.extensions.toMb
import com.doneit.ascend.presentation.utils.extensions.toNotificationDate

class AttachmentImageViewHolder(
    private val binding: ListItemAttachmentImageBinding
) : AttachmentViewHolder(binding.root) {
    fun bind(item: AttachmentEntity, onDownloadClick: (AttachmentEntity) -> Unit){
        binding.item = item
        binding.date = item.createdAt.toNotificationDate()
        binding.download.setOnClickListener{
            onDownloadClick.invoke(item)
        }
        binding.fileSize = item.fileSize.toMb()
        binding.executePendingBindings()
    }

    companion object {
        fun create(parent: ViewGroup): AttachmentImageViewHolder {
            val binding: ListItemAttachmentImageBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.list_item_attachment_image,
                parent,
                false
            )

            return AttachmentImageViewHolder(binding)
        }
    }
}