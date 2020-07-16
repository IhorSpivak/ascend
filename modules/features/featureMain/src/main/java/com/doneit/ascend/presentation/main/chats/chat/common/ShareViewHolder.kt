package com.doneit.ascend.presentation.main.chats.chat.common

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions
import com.doneit.ascend.domain.entity.chats.MemberEntity
import com.doneit.ascend.domain.entity.chats.MessageEntity
import com.doneit.ascend.domain.entity.community_feed.Attachment
import com.doneit.ascend.domain.entity.community_feed.ContentType
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.common.gone
import com.doneit.ascend.presentation.main.common.visible
import com.doneit.ascend.presentation.main.databinding.ListItemSharedMessageBinding
import com.doneit.ascend.presentation.utils.extensions.visibleOrGone

class ShareViewHolder(
    itemView: View
) : BaseMessageHolder(itemView) {

    private val binding: ListItemSharedMessageBinding = DataBindingUtil.getBinding(itemView)!!
    override fun bind(
        messageEntity: MessageEntity,
        nextMessage: MessageEntity?,
        memberEntity: MemberEntity,
        chatOwner: MemberEntity,
        currentUserId: Long
    ) {
        with(binding) {
            member = memberEntity
            this.messageEntity = messageEntity
            messageEntity.post?.let {
                postModel = it
                isLiked = it.isLikedMe
                likesCount = it.likesCount
                commentsCount = it.commentsCount
                isOwner = it.isOwner
                setupAttachments(it.attachments)
            }
        }
    }

    private fun ListItemSharedMessageBinding.setupAttachments(attachments: List<Attachment>) {
        if (attachments.isEmpty()) postView.mivAttachments.gone()
        postView.imvFirst.glideLoad(attachments.getOrNull(0))
        postView.imvSecond.glideLoad(attachments.getOrNull(1))
        postView.imvThird.glideLoad(attachments.getOrNull(2))
    }

    private fun ImageView.glideLoad(attachment: Attachment?) {
        visibleOrGone(attachment != null)
        attachment ?: return
        binding.postView.mivAttachments.visible()
        when (attachment.contentType) {
            ContentType.IMAGE -> loadImage(attachment.url)
            ContentType.VIDEO -> loadImage(attachment.url)
        }
    }

    private fun ImageView.loadImage(url: String) {
        Glide.with(this)
            .asBitmap()
            .load(url)
            .error(R.drawable.ic_action_block)
            .centerCrop()
            .placeholder(ColorDrawable(Color.LTGRAY))
            .transition(BitmapTransitionOptions.withCrossFade())
            .into(this)
    }

    companion object {
        fun create(parent: ViewGroup): ShareViewHolder {
            return ShareViewHolder(
                DataBindingUtil.inflate<ListItemSharedMessageBinding>(
                    LayoutInflater.from(parent.context),
                    R.layout.list_item_shared_message,
                    parent,
                    false
                ).root
            )
        }
    }
}