package com.doneit.ascend.presentation.video_chat.attachments.common

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.doneit.ascend.domain.entity.AttachmentEntity
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.databinding.ListItemAttachmentImageBinding
import com.doneit.ascend.presentation.utils.extensions.toAttachmentDate
import com.doneit.ascend.presentation.utils.extensions.toMb

class AttachmentImageViewHolder(
    private val binding: ListItemAttachmentImageBinding,
    private val onPreviewClick: (AttachmentEntity) -> Unit
) : AttachmentViewHolder(binding.root) {
    fun bind(
        item: AttachmentEntity,
        onDownloadClick: (AttachmentEntity) -> Unit,
        onDeleteListener: (id: Long) -> Unit
    ) {
        binding.item = item
        binding.date = item.createdAt.toAttachmentDate()
        binding.fileSize = item.fileSize.toMb()

        binding.download.setOnClickListener {
            if (!isFileExist(item.fileName)) {
                onDownloadClick.invoke(item)
            } else onPreviewClick(item)
        }
        binding.ibDelete.setOnClickListener {
            onDeleteListener.invoke(item.id)
        }
    }

    companion object {
        fun create(
            parent: ViewGroup,
            onPreviewClick: (AttachmentEntity) -> Unit
        ): AttachmentImageViewHolder {
            val binding: ListItemAttachmentImageBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.list_item_attachment_image,
                parent,
                false
            )
            return AttachmentImageViewHolder(binding, onPreviewClick)
        }
    }
}