package com.doneit.ascend.presentation.main.home.community_feed.diffutil

import androidx.recyclerview.widget.DiffUtil
import com.doneit.ascend.domain.entity.community_feed.Post

class PostDiffUtilCallback : DiffUtil.ItemCallback<Post>() {
    override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean {
        return oldItem == newItem
    }

}