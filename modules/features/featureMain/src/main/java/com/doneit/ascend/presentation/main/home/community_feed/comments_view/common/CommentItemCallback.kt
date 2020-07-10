package com.doneit.ascend.presentation.main.home.community_feed.comments_view.common

import androidx.recyclerview.widget.DiffUtil
import com.doneit.ascend.domain.entity.community_feed.Comment

class CommentItemCallback : DiffUtil.ItemCallback<Comment>() {
    override fun areItemsTheSame(oldItem: Comment, newItem: Comment): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Comment, newItem: Comment): Boolean {
        return oldItem == newItem
    }

}