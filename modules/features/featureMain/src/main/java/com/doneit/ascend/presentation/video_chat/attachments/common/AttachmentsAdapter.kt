package com.doneit.ascend.presentation.video_chat.attachments.common

import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import com.doneit.ascend.domain.entity.AttachmentEntity
import com.doneit.ascend.domain.entity.AttachmentType

class AttachmentsAdapter(
    private val onDownloadClick: (model: AttachmentEntity) -> Unit,
    private val onCopyClick: (model: AttachmentEntity) -> Unit,
    private val onDeleteListener: (id: Long) -> Unit,
    private val onPreviewClick: (AttachmentEntity) -> Unit
) :
    PagedListAdapter<AttachmentEntity, AttachmentViewHolder>(AttachmentDiffCallback()) {

    init {
        setHasStableIds(true)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AttachmentViewHolder {
        return when (viewType) {
            0 -> AttachmentFileViewHolder.create(parent, onPreviewClick)
            1 -> AttachmentImageViewHolder.create(parent, onPreviewClick)
            else -> AttachmentLinkViewHolder.create(parent)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)?.attachmentType) {
            AttachmentType.FILE -> 0
            AttachmentType.IMAGE -> 1
            else -> 2
        }
    }

    override fun onBindViewHolder(holder: AttachmentViewHolder, position: Int) {
        val model = getItem(position)
        when (model?.attachmentType) {
            AttachmentType.FILE -> {
                (holder as AttachmentFileViewHolder).bind(
                    model,
                    onDownloadClick,
                    { onDeleteListener.invoke(it) })
            }
            AttachmentType.IMAGE -> {
                (holder as AttachmentImageViewHolder).bind(
                    model,
                    onDownloadClick,
                    { onDeleteListener.invoke(it) })
            }
            //TODO: is link type is exist??
            else -> {
                (holder as AttachmentLinkViewHolder).bind(
                    model!!,
                    onCopyClick,
                    { onDeleteListener.invoke(it) })
            }
        }
    }
}