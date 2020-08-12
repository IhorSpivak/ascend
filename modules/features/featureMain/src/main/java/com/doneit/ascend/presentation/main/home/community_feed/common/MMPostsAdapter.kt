package com.doneit.ascend.presentation.main.home.community_feed.common


import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.doneit.ascend.domain.entity.chats.ChatEntity
import com.doneit.ascend.domain.entity.community_feed.Post
import com.doneit.ascend.domain.entity.user.UserEntity
import com.doneit.ascend.domain.use_case.PagedList
import com.doneit.ascend.presentation.common.PaginationAdapter
import com.doneit.ascend.presentation.main.home.community_feed.diffutil.ChannelDiffUtilCallback
import com.doneit.ascend.presentation.main.home.community_feed.diffutil.PostDiffUtilCallback

class MMPostsAdapter(
    private val onPostClickListeners: MMPostClickListener,
    private val user: UserEntity
) : PaginationAdapter<Post, MMPostViewHolder>(PostDiffUtilCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MMPostViewHolder {
        return MMPostViewHolder.create(parent, onPostClickListeners)
    }

    override fun onBindViewHolder(holder: MMPostViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}