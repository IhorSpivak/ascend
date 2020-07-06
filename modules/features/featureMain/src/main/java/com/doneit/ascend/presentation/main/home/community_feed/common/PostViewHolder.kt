package com.doneit.ascend.presentation.main.home.community_feed.common

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.doneit.ascend.domain.entity.community_feed.Post
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.databinding.ListItemFeedBinding

class PostViewHolder(
    itemView: View,
    private val postClickListeners: PostClickListeners
) : RecyclerView.ViewHolder(itemView) {

    private val binding: ListItemFeedBinding = DataBindingUtil.getBinding(itemView)!!

    fun bind(post: Post) {
        with(binding) {
            postModel = post
            mivAttachments.setAttachments(post.attachments)
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