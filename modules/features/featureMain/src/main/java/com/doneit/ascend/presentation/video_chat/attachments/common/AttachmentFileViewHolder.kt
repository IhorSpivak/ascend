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
    private val binding: ListItemAttachmentFileBinding
) : AttachmentViewHolder(binding.root) {


    fun bind(item: AttachmentEntity, onDownloadClick: (AttachmentEntity) -> Unit, onDeleteListener: (id: Long) -> Unit){
        binding.item = item
        binding.fileSize = item.fileSize.toMb()
        binding.download.setOnClickListener{
            onDownloadClick.invoke(item)
        }
        binding.date = item.createdAt.toAttachmentDate()
        binding.ibDelete.setOnClickListener {
            onDeleteListener.invoke(item.id!!)
        }
        binding.executePendingBindings()
    }

    companion object {
        fun create(parent: ViewGroup): AttachmentFileViewHolder {
            val binding: ListItemAttachmentFileBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.list_item_attachment_file,
                parent,
                false
            )

            return AttachmentFileViewHolder(binding)
        }
    }
}