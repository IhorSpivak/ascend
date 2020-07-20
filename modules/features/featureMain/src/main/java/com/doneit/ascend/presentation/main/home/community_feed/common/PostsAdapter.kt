package com.doneit.ascend.presentation.main.home.community_feed.common

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.doneit.ascend.domain.entity.chats.ChatEntity
import com.doneit.ascend.domain.entity.community_feed.Post
import com.doneit.ascend.domain.entity.user.UserEntity
import com.doneit.ascend.domain.use_case.PagedList
import com.doneit.ascend.presentation.common.PaginationAdapter
import com.doneit.ascend.presentation.main.home.community_feed.diffutil.PostDiffUtilCallback

class PostsAdapter(
    private val onPostClickListeners: PostClickListeners,
    private val user: UserEntity
) : PaginationAdapter<Post, RecyclerView.ViewHolder>(PostDiffUtilCallback(), 1) {

    private var channelList: PagedList<ChatEntity>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_HEADER -> PostsHeaderViewHolder.create(parent, user, onPostClickListeners)
            TYPE_OTHER -> PostViewHolder.create(parent, onPostClickListeners)
            else -> throw IllegalArgumentException("Unsupported view type: $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            TYPE_HEADER -> {
                (holder as PostsHeaderViewHolder).bind(channelList ?: return)
            }
            TYPE_OTHER -> {
                (holder as PostViewHolder).bind(getItem(position))
            }
            else -> throw IllegalArgumentException(
                "Unsupported view type: ${getItemViewType(position)}"
            )
        }
    }

    fun submitChannels(channelList: PagedList<ChatEntity>) {
        this.channelList = channelList
        notifyItemChanged(0, channelList)
    }


    override fun getItemViewType(position: Int): Int {
        return when (position) {
            0 -> TYPE_HEADER
            else -> TYPE_OTHER
        }
    }

    companion object {
        private const val TYPE_HEADER = 1
        private const val TYPE_OTHER = 2
    }

}