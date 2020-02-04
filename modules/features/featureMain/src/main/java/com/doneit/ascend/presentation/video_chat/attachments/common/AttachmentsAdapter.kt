package com.doneit.ascend.presentation.video_chat.attachments.common

import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import com.doneit.ascend.domain.entity.AttachmentEntity
import com.doneit.ascend.domain.entity.AttachmentType

class AttachmentsAdapter(private val onDownloadClick: (model: AttachmentEntity)->Unit,
                         private val onCopyClick: (model: AttachmentEntity)->Unit ) :
    PagedListAdapter<AttachmentEntity, AttachmentViewHolder>(AttachmentDiffCallback()) {

    init {
        setHasStableIds(true)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AttachmentViewHolder {
        return when (viewType) {
            0 -> AttachmentFileViewHolder.create(parent)
            1 -> AttachmentImageViewHolder.create(parent)
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
        val model =  getItem(position)
        when (model?.attachmentType) {
            AttachmentType.FILE -> {
                (holder as AttachmentFileViewHolder).bind(model, onDownloadClick)
                holder.itemView.setOnClickListener {
                    //onGroupClick.invoke(model)
                }
            }
            AttachmentType.IMAGE -> {
                (holder as AttachmentImageViewHolder).bind(model, onDownloadClick)
                holder.itemView.setOnClickListener {
                    //onGroupClick.invoke(model)
                }
            }

            //TODO: is link type is exist??
            else -> {
                (holder as AttachmentLinkViewHolder).bind(model!!, onCopyClick)
                holder.itemView.setOnClickListener {
                    //onGroupClick.invoke(model)
                }
            }
        }
    }

}