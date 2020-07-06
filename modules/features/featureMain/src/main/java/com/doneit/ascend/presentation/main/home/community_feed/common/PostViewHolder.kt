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
import com.doneit.ascend.domain.entity.community_feed.Post
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.common.gone
import com.doneit.ascend.presentation.main.common.visible
import com.doneit.ascend.presentation.main.databinding.ListItemFeedBinding
import com.doneit.ascend.presentation.utils.extensions.visibleOrGone

class PostViewHolder(
    itemView: View,
    private val postClickListeners: PostClickListeners
) : RecyclerView.ViewHolder(itemView) {

    private val binding: ListItemFeedBinding = DataBindingUtil.getBinding(itemView)!!

    fun bind(post: Post) {
        with(binding) {
            postModel = post
            setupAttachments(post.attachments)
            mmiAvatar.setOnClickListener { postClickListeners.onUserClick(1L) }
            tvName.setOnClickListener { postClickListeners.onUserClick(1L) }
            btnLike.setOnClickListener { postClickListeners.onLikeClick(post.id) }
            btnShare.setOnClickListener { postClickListeners.onShareClick(post.id) }
            btnSend.setOnClickListener {
                postClickListeners.onSendCommentClick(
                    post.id,
                    etInputMessage.text?.toString().orEmpty()
                )
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
        visibleOrGone(attachment == null)
        attachment ?: return
        binding.mivAttachments.visible()
        Glide.with(this)
            .asBitmap()
            .load(attachment.url)
            .error(R.drawable.ic_action_block)
            .centerCrop()
            .placeholder(ColorDrawable(Color.LTGRAY))
            .transition(BitmapTransitionOptions.withCrossFade())
            .into(this)
    }

    companion object {
        fun create(parent: ViewGroup, postClickListeners: PostClickListeners): PostViewHolder {
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