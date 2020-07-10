package com.doneit.ascend.presentation.main.home.community_feed.common

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions
import com.doneit.ascend.domain.entity.community_feed.Attachment
import com.doneit.ascend.domain.entity.community_feed.ContentType
import com.doneit.ascend.domain.entity.community_feed.Post
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.common.gone
import com.doneit.ascend.presentation.main.common.visible
import com.doneit.ascend.presentation.main.databinding.ListItemFeedBinding
import com.doneit.ascend.presentation.utils.extensions.hideKeyboard
import com.doneit.ascend.presentation.utils.extensions.visibleOrGone

class PostViewHolder(
    itemView: View,
    private val postClickListeners: PostClickListeners
) : RecyclerView.ViewHolder(itemView) {

    private val binding: ListItemFeedBinding = DataBindingUtil.getBinding(itemView)!!

    fun bind(post: Post) {
        with(binding) {
            postModel = post
            isLiked = post.isLikedMe
            likesCount = post.likesCount
            commentsCount = post.commentsCount
            isOwner = post.isOwner
            setupAttachments(post.attachments)
            setClickListeners(post)
        }
    }

    fun updateFromPayloads(payloads: List<Any>) {
        if (payloads.isEmpty()) return
        val payload = (payloads.first() as? PostPayload) ?: return
        with(binding) {
            payload.likeStatus?.let {
                likesCount = if (it) {
                    likesCount?.inc()
                } else likesCount?.dec()
                postModel?.isLikedMe = it
                isLiked = it
                postModel?.likesCount = likesCount ?: 0
            }
            payload.commentsCount?.let {
                commentsCount = commentsCount?.plus(it)
            }
        }
    }

    private fun ListItemFeedBinding.setClickListeners(
        post: Post
    ) {
        mmiAvatar.setOnClickListener {
            postClickListeners.onUserClick(post.owner.id)
        }
        tvName.setOnClickListener {
            postClickListeners.onUserClick(post.owner.id)
        }
        btnLike.setOnClickListener {
            postClickListeners.onLikeClick(post.isLikedMe, post.id, adapterPosition)
            post.isLikedMe = !post.isLikedMe
        }
        btnShare.setOnClickListener {
            postClickListeners.onShareClick(post.id)
        }
        btnBlock.setOnClickListener {
            if (post.isOwner) {
                postClickListeners.onOptionsClick(post)
            } else postClickListeners.onComplainClick(post.id)
        }
        btnSend.setOnClickListener {
            if (etInputMessage.length() > 2) {
                postClickListeners.onSendCommentClick(
                    post.id,
                    etInputMessage.text?.toString().orEmpty(),
                    adapterPosition
                )
                etInputMessage.text?.clear()
                etInputMessage.hideKeyboard()
            }
        }
    }

    private fun ListItemFeedBinding.setupAttachments(attachments: List<Attachment>) {
        if (attachments.isEmpty()) mivAttachments.gone()
        imvFirst.glideLoad(attachments.getOrNull(0))
        imvSecond.glideLoad(attachments.getOrNull(1))
        imvThird.glideLoad(attachments.getOrNull(2))
    }

    private fun ImageView.glideLoad(attachment: Attachment?) {
        visibleOrGone(attachment != null)
        attachment ?: return
        binding.mivAttachments.visible()
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

    private fun ImageView.loadFirstVideoFrame(url: String) {
        Glide.with(this)
            .asBitmap()
            .load(url)
            .error(R.drawable.ic_action_block)
            .centerCrop()
            .placeholder(ColorDrawable(Color.LTGRAY))
            .transition(BitmapTransitionOptions.withCrossFade())
            .into(this)
    }

    data class PostPayload(
        val likeStatus: Boolean?,
        val commentsCount: Int?
    )

    companion object {

        fun buildPayload(
            likeStatus: Boolean? = null,
            commentsCount: Int? = null
        ) = PostPayload(likeStatus, commentsCount)

        fun create(
            parent: ViewGroup,
            postClickListeners: PostClickListeners
        ): PostViewHolder {
            return PostViewHolder(
                DataBindingUtil.inflate<ListItemFeedBinding>(
                    LayoutInflater.from(parent.context),
                    R.layout.list_item_feed,
                    parent,
                    false
                ).root,
                postClickListeners
            )
        }
    }
}