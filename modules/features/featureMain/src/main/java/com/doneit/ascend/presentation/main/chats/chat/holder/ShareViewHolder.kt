package com.doneit.ascend.presentation.main.chats.chat.holder

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
import com.doneit.ascend.domain.entity.community_feed.Post
import com.doneit.ascend.domain.entity.community_feed.PostNullable
import com.doneit.ascend.presentation.common.setOnSingleClickListener
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.common.gone
import com.doneit.ascend.presentation.main.common.visible
import com.doneit.ascend.presentation.main.databinding.ListItemSharedMessageBinding
import com.doneit.ascend.presentation.utils.extensions.visibleOrGone

class ShareViewHolder private constructor(
    itemView: View,
    private val onSeeMoreClick: (Post) -> Unit
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
            postView.viewPostContent.visibilityOfSeeAll = true
            postView.viewPostContent.tvSeeMore.setOnSingleClickListener {
                onSeeMoreClick(
                    messageEntity.post ?: return@setOnSingleClickListener
                )
            }
            PostNullable.create(messageEntity.post).let {
                postModel = it
                isOwner = it.isOwner == true
                it.attachments?.let { attachments -> setupAttachments(attachments) }
            }
        }
    }

    private fun ListItemSharedMessageBinding.setupAttachments(attachments: List<Attachment>) {
        if (attachments.isEmpty()) postView.viewPostContent.mivAttachments.gone()
        postView.viewPostContent.imvFirst.glideLoad(attachments.getOrNull(0))
        postView.viewPostContent.imvSecond.glideLoad(attachments.getOrNull(1))
        postView.viewPostContent.imvThird.glideLoad(attachments.getOrNull(2))
    }

    private fun ImageView.glideLoad(attachment: Attachment?) {
        visibleOrGone(attachment != null)
        attachment ?: return
        binding.postView.viewPostContent.mivAttachments.visible()
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
        fun create(parent: ViewGroup, onSeeMoreClick: (Post) -> Unit): ShareViewHolder {
            return ShareViewHolder(
                DataBindingUtil.inflate<ListItemSharedMessageBinding>(
                    LayoutInflater.from(parent.context),
                    R.layout.list_item_shared_message,
                    parent,
                    false
                ).root,
                onSeeMoreClick
            )
        }
    }
}