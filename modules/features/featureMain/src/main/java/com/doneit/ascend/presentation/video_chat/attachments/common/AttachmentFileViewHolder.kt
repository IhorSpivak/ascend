package com.doneit.ascend.presentation.video_chat.attachments.common

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.doneit.ascend.domain.entity.AttachmentEntity
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.databinding.ListItemAttachmentFileBinding
import com.doneit.ascend.presentation.utils.extensions.toAttachmentDate
import com.doneit.ascend.presentation.utils.extensions.toMb

class AttachmentFileViewHolder(
    private val binding: ListItemAttachmentFileBinding,
    private val onPreviewClick: (AttachmentEntity) -> Unit
) : AttachmentViewHolder(binding.root) {


    fun bind(
        item: AttachmentEntity,
        onDownloadClick: (AttachmentEntity) -> Unit,
        onDeleteListener: (id: Long) -> Unit
    ) {
        binding.item = item
        binding.fileSize = item.fileSize.toMb()
        binding.date = item.createdAt.toAttachmentDate()
        binding.download.setImageResource(
            if (isFileExist(item.fileName))
                R.drawable.ic_sent_message
            else R.drawable.ic_download
        )
        binding.root.setOnClickListener {
            if (!isFileExist(item.fileName))
                onDownloadClick.invoke(item)
            else onPreviewClick(item)
        }
        binding.ibDelete.setOnClickListener {
            onDeleteListener.invoke(item.id)
        }
    }

    companion object {
        fun create(
            parent: ViewGroup,
            onPreviewClick: (AttachmentEntity) -> Unit
        ): AttachmentFileViewHolder {
            val binding: ListItemAttachmentFileBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.list_item_attachment_file,
                parent,
                false
            )
            return AttachmentFileViewHolder(binding, onPreviewClick)
        }
    }
}