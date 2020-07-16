package com.doneit.ascend.presentation.main.home.community_feed.create_post.common

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.doneit.ascend.domain.entity.community_feed.Attachment
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.databinding.ListItemGroupAttachmentVideoBinding
import com.doneit.ascend.presentation.utils.extensions.getDurationBreakdown

class VideoAttachmentHolder(
    itemView: View,
    private val onDeleteClick: (Int) -> Unit
) : AttachmentHolder(itemView) {

    private val binding: ListItemGroupAttachmentVideoBinding = DataBindingUtil.bind(itemView)!!

    override fun bind(attachment: Attachment) {
        binding.apply {
            vvVideo.setVideoURI(Uri.parse(attachment.url))
            vvVideo.setOnPreparedListener {
                duration.text = it.duration.getDurationBreakdown()
                it.seekTo(1)
            }
            vvVideo.setOnCompletionListener {
                isPlaying = false
            }
            btnPlay.setOnClickListener {
                if (!vvVideo.isPlaying) vvVideo.start() else vvVideo.pause()
                isPlaying = vvVideo.isPlaying
            }
            vvVideo.setOnClickListener {
                if (!vvVideo.isPlaying) vvVideo.start() else vvVideo.pause()
                isPlaying = vvVideo.isPlaying
            }
            btnDelete.setOnClickListener { onDeleteClick(adapterPosition) }
        }
    }

    companion object {
        fun create(parent: ViewGroup, onDeleteClick: (Int) -> Unit): VideoAttachmentHolder {
            return VideoAttachmentHolder(
                DataBindingUtil.inflate<ListItemGroupAttachmentVideoBinding>(
                    LayoutInflater.from(parent.context),
                    R.layout.list_item_group_attachment_video,
                    parent,
                    false
                ).root,
                onDeleteClick
            )
        }
    }
}