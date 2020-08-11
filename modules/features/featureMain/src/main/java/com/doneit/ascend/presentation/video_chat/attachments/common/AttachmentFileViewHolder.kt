package com.doneit.ascend.presentation.video_chat.attachments.common

import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.databinding.DataBindingUtil
import com.doneit.ascend.domain.entity.AttachmentEntity
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.databinding.ListItemAttachmentFileBinding
import com.doneit.ascend.presentation.utils.extensions.toAttachmentDate
import com.doneit.ascend.presentation.utils.extensions.toMb
import java.io.File

class AttachmentFileViewHolder(
    private val binding: ListItemAttachmentFileBinding
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
        }
        binding.ibDelete.setOnClickListener {
            onDeleteListener.invoke(item.id)
        }
    }

    private fun isFileExist(filename: String): Boolean {
        val dir = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            MediaStore.Downloads.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
        } else Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            .toUri()
        return File("${dir.path}", filename).exists()
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