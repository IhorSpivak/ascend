package com.doneit.ascend.presentation.main.home.community_feed.common

import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import com.doneit.ascend.domain.entity.community_feed.Post
import com.doneit.ascend.presentation.main.home.community_feed.diffutil.PostDiffUtilCallback

class PostsAdapter(
    private val onPostClickListeners: PostClickListeners
) : PagedListAdapter<Post, PostViewHolder>(PostDiffUtilCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        return PostViewHolder.create(parent, onPostClickListeners)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it)
        }
    }

}