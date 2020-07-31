package com.doneit.ascend.presentation.main.chats.chat.holder

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.doneit.ascend.domain.entity.AttachmentType
import com.doneit.ascend.domain.entity.chats.MemberEntity
import com.doneit.ascend.domain.entity.chats.MessageEntity
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.databinding.ListItemOwnMessageAttachmentBinding
import com.doneit.ascend.presentation.utils.extensions.MESSAGE_FORMATTER
import com.doneit.ascend.presentation.utils.extensions.calculateDate
import com.doneit.ascend.presentation.utils.extensions.toDefaultFormatter
import com.doneit.ascend.presentation.utils.extensions.visibleOrGone
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util

class AttachmentOwnViewHolder private constructor(
    itemView: View,
    private val onDeleteClick: (message: MessageEntity) -> Unit
) : BaseAttachmentHolder(itemView) {


    override fun bind(
        messageEntity: MessageEntity,
        nextMessage: MessageEntity?,
        memberEntity: MemberEntity,
        chatOwner: MemberEntity,
        currentUserId: Long
    ) {
        DataBindingUtil.bind<ListItemOwnMessageAttachmentBinding>(itemView)?.apply {
            this.messageEntity = messageEntity
            ibDelete.setOnClickListener {
                onDeleteClick(messageEntity)
            }
            time.apply {
                text = MESSAGE_FORMATTER.toDefaultFormatter().format(messageEntity.createdAt!!)
                visibleOrGone(
                    nextMessage == null || calculateDate(
                        messageEntity.createdAt!!,
                        nextMessage.createdAt!!
                    )
                )
            }
            val attachment = messageEntity.attachment ?: return
            mediaContainer.visibleOrGone(attachment.type != AttachmentType.FILE)
            attachmentImage.visibleOrGone(attachment.type == AttachmentType.IMAGE)
            attachmentVideo.visibleOrGone(attachment.type == AttachmentType.VIDEO)
            attachmentFile.visibleOrGone(attachment.type == AttachmentType.FILE)
            val res = if (!isFileExist(attachment.name)) {
                R.drawable.ic_download
            } else R.drawable.ic_sent_message
            download.setImageResource(res)
            download.setOnClickListener {
                if (!isFileExist(attachment.name)) {
                    downloadFile(attachment.url, attachment.name)
                }
            }
            when (attachment.type) {
                AttachmentType.VIDEO -> {
                    val player = SimpleExoPlayer.Builder(itemView.context)
                        .build()
                    attachmentVideo.player = player
                    player.playWhenReady = false
                    player.prepare(createMediaSource(itemView.context, attachment.url))
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
        DataBindingUtil.bind<ListItemOwnMessageAttachmentBinding>(itemView)?.apply {
            download.setImageResource(R.drawable.ic_sent_message)
        }
    }

    companion object {
        fun create(
            parent: ViewGroup,
            onDeleteClick: (message: MessageEntity) -> Unit
        ): AttachmentOwnViewHolder {
            return AttachmentOwnViewHolder(
                DataBindingUtil.inflate<ListItemOwnMessageAttachmentBinding>(
                    LayoutInflater.from(parent.context),
                    R.layout.list_item_own_message_attachment,
                    parent,
                    false
                ).root,
                onDeleteClick
            )
        }
    }
}