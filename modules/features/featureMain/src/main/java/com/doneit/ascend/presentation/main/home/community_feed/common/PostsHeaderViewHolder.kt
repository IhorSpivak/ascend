package com.doneit.ascend.presentation.main.home.community_feed.common

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.doneit.ascend.domain.entity.chats.ChatEntity
import com.doneit.ascend.domain.entity.user.UserEntity
import com.doneit.ascend.domain.use_case.PagedList
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.databinding.ListItemPostsHeaderBinding

class PostsHeaderViewHolder(
    itemView: View,
    private val user: UserEntity,
    private val postClickListeners: PostClickListeners
) : RecyclerView.ViewHolder(itemView) {

    private val binding: ListItemPostsHeaderBinding = DataBindingUtil.bind(itemView)!!
    private val adapter = ChannelAdapter(postClickListeners.onChannelClick).apply {
        binding.rvChannels.adapter = this
    }

    fun bind() {
        binding.apply {
            onNewPostClick = View.OnClickListener { postClickListeners.onCreatePostListener() }
            onSeAllClick = View.OnClickListener { postClickListeners.onSeeAllClickListener() }
            user = this@PostsHeaderViewHolder.user
        }
    }

    fun bind(channelList: PagedList<ChatEntity>) {
        bind()
        adapter.submitList(channelList)
    }

    companion object {
        fun create(
            parent: ViewGroup,
            user: UserEntity,
            postClickListeners: PostClickListeners
        ): PostsHeaderViewHolder {
            return PostsHeaderViewHolder(
                DataBindingUtil.inflate<ListItemPostsHeaderBinding>(
                    LayoutInflater.from(parent.context),
                    R.layout.list_item_posts_header,
                    parent,
                    false
                ).root,
                user,
                postClickListeners
            )
        }
    }
}