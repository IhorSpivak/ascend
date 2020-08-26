package com.doneit.ascend.presentation.main.chats.chat.holder

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.doneit.ascend.domain.entity.AttachmentType
import com.doneit.ascend.domain.entity.MessageAttachment
import com.doneit.ascend.domain.entity.chats.MemberEntity
import com.doneit.ascend.domain.entity.chats.MessageEntity
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.databinding.ListItemOtherMessageAttachmentBinding
import com.doneit.ascend.presentation.utils.extensions.*
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util

class AttachmentOtherViewHolder private constructor(
    itemView: View,
    private val previewAttachment: (MessageAttachment) -> Unit
) : BaseAttachmentHolder(itemView) {


    fun bind(
        messageEntity: MessageEntity,
        nextMessage: MessageEntity?,
        memberEntity: MemberEntity
    ) {
        DataBindingUtil.bind<ListItemOtherMessageAttachmentBinding>(itemView)?.apply {
            this.messageEntity = messageEntity
            this.memberEntity = memberEntity
            time.apply {
                text = MESSAGE_FORMATTER.toDefaultFormatter().format(messageEntity.createdAt)
                visibleOrGone(
                    nextMessage == null || calculateDate(
                        messageEntity.createdAt,
                        nextMessage.createdAt
                    )
                )
            }
            if(messageEntity.createdAt != null) {
                messageTime.text = root.context.getTimeFormat().format(messageEntity.createdAt)
            }
            val attachment = messageEntity.attachment ?: return
            mediaContainer.visibleOrGone(attachment.type != AttachmentType.FILE)
            attachmentImage.visibleOrGone(attachment.type == AttachmentType.IMAGE)
            attachmentVideo.visibleOrGone(attachment.type == AttachmentType.VIDEO)
            fabPlay.visibleOrGone(attachment.type == AttachmentType.VIDEO)
            attachmentFile.visibleOrGone(attachment.type == AttachmentType.FILE)
            val res = if (!isFileExist(attachment.name)) {
                R.drawable.ic_download
            } else R.drawable.ic_sent_message
            download.setImageResource(res)
            messageContainer.setOnClickListener {
                if (!isFileExist(attachment.name)) {
                    downloadFile(attachment.url, attachment.name)
                } else previewAttachment(attachment)
            }
            when (attachment.type) {
                AttachmentType.VIDEO -> {
                    val player = SimpleExoPlayer.Builder(itemView.context)
                        .build()
                    attachmentVideo.player = player
                    player.playWhenReady = false
                    player.prepare(createMediaSource(itemView.context, attachment.url))
                    attachmentVideo.setOnClickListener { previewAttachment(attachment) }
                }
                else -> Unit
            }
        }
    }

    private fun createMediaSource(context: Context, url: String): MediaSource {
        return ProgressiveMediaSource.Factory(
            DefaultDataSourceFactory(
                context,
                Util.getUserAgent(context, context.getString(R.string.app_name))
            )
        ).createMediaSource(Uri.parse(url))
    }

    override fun resourceDownloaded() {
        DataBindingUtil.bind<ListItemOtherMessageAttachmentBinding>(itemView)?.apply {
            download.setImageResource(R.drawable.ic_sent_message)
        }
    }

    companion object {
        fun create(
            parent: ViewGroup,
            previewAttachment: (MessageAttachment) -> Unit
        ): AttachmentOtherViewHolder {
            return AttachmentOtherViewHolder(
                DataBindingUtil.inflate<ListItemOtherMessageAttachmentBinding>(
                    LayoutInflater.from(parent.context),
                    R.layout.list_item_other_message_attachment,
                    parent,
                    false
                ).root,
                previewAttachment
            )
        }
    }
}