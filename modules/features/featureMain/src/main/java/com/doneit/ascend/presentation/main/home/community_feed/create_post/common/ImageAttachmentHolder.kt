package com.doneit.ascend.presentation.main.home.community_feed.create_post.common

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.doneit.ascend.domain.entity.community_feed.Attachment
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.databinding.ListItemGroupAttachmentImageBinding

class ImageAttachmentHolder private constructor(
    itemView: View,
    private val onDeleteClick: (Int) -> Unit
) : AttachmentHolder(itemView) {

    private val binding: ListItemGroupAttachmentImageBinding = DataBindingUtil.bind(itemView)!!

    override fun bind(attachment: Attachment) {
        binding.attachment = attachment
        binding.btnDelete.setOnClickListener { onDeleteClick(adapterPosition) }
    }

    companion object {
        fun create(
            parent: ViewGroup,
            onDeleteClick: (Int) -> Unit
        ): ImageAttachmentHolder {
            return ImageAttachmentHolder(
                DataBindingUtil.inflate<ListItemGroupAttachmentImageBinding>(
                    LayoutInflater.from(parent.context),
                    R.layout.list_item_group_attachment_image,
                    parent,
                    false
                ).root,
                onDeleteClick
            )
        }
    }
}