package com.doneit.ascend.presentation.main.home.community_feed.common

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.doneit.ascend.domain.entity.community_feed.Post
import com.doneit.ascend.presentation.common.setOnSingleClickListener
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.databinding.ListItemFeedBinding
import com.doneit.ascend.presentation.main.databinding.ViewPostContentBinding
import com.doneit.ascend.presentation.utils.applyFilter
import com.doneit.ascend.presentation.utils.extensions.hideKeyboard

class PostViewHolder(
    itemView: View,
    private val postClickListeners: PostClickListeners
) : RecyclerView.ViewHolder(itemView) {

    private val binding: ListItemFeedBinding = DataBindingUtil.getBinding(itemView)!!

    fun bind(post: Post) {
        with(binding) {
            postModel = post
            visibilityOfSend = true
            viewPostContent.setupAttachments(post.attachments)
            viewPostContent.applyResizing(post.attachments)
            viewPostContent.setClickListeners(post)
        }
    }

    private fun ViewPostContentBinding.setClickListeners(
        post: Post
    ) {
        btnComments.setOnSingleClickListener {
            postClickListeners.onCommentClick(post.id)
        }
        mmiAvatar.setOnSingleClickListener {
            postClickListeners.onUserClick(post.owner.id)
        }
        tvName.setOnSingleClickListener {
            postClickListeners.onUserClick(post.owner.id)
        }
        btnLike.setOnSingleClickListener {
            postClickListeners.onLikeClick(post.isLikedMe, post.id, adapterPosition)
            post.isLikedMe = !post.isLikedMe
        }
        btnShare.setOnSingleClickListener {
            postClickListeners.onShareClick(post.id)
        }
        btnBlock.setOnSingleClickListener {
            if (post.isOwner) {
                postClickListeners.onOptionsClick(it, post)
            } else postClickListeners.onComplainClick(post.owner.id)
        }
        btnSend.setOnSingleClickListener {
            if (etInputMessage.length() > 2) {
                postClickListeners.onSendCommentClick(
                    post.id,
                    etInputMessage.text?.toString().orEmpty().trim(),
                    adapterPosition
                )
                etInputMessage.text?.clear()
                etInputMessage.hideKeyboard()
            }
        }
        etInputMessage.applyFilter()
        imvFirst.setOnSingleClickListener { postClickListeners.onMediaClick(post.attachments, 0) }
        imvSecond.setOnSingleClickListener { postClickListeners.onMediaClick(post.attachments, 1) }
        imvThird.setOnSingleClickListener { postClickListeners.onMediaClick(post.attachments, 2) }
        imvFourth.setOnSingleClickListener { postClickListeners.onMediaClick(post.attachments, 3) }
        imvFifth.setOnSingleClickListener { postClickListeners.onMediaClick(post.attachments, 4) }
    }

    companion object {

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