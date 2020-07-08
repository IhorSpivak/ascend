package com.doneit.ascend.presentation.main.home.community_feed.common

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.doneit.ascend.domain.entity.community_feed.Channel
import com.doneit.ascend.domain.entity.community_feed.Post
import com.doneit.ascend.domain.entity.user.UserEntity
import com.doneit.ascend.domain.use_case.PagedList
import com.doneit.ascend.presentation.common.PaginationAdapter
import com.doneit.ascend.presentation.main.home.community_feed.diffutil.PostDiffUtilCallback

class PostsAdapter(
    private val onPostClickListeners: PostClickListeners,
    private val user: UserEntity
) : PaginationAdapter<Post, RecyclerView.ViewHolder>(PostDiffUtilCallback()) {

    private var channelList: PagedList<Channel>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_HEADER -> PostsHeaderViewHolder.create(parent, user, onPostClickListeners)
            TYPE_OTHER -> PostViewHolder.create(
                parent,
                onPostClickListeners.copy(
                    onLikeClick = { isLiked, id, i ->
                        onPostClickListeners.onLikeClick(isLiked, id, i)
                        notifyItemChanged(
                            i,
                            PostViewHolder.buildPayload(!isLiked)
                        )
                    },
                    onSendCommentClick = { id, text, i ->
                        onPostClickListeners.onSendCommentClick(id, text, i)
                        notifyItemChanged(
                            i,
                            PostViewHolder.buildPayload(commentsCount = 1)
                        )
                    }
                )
            )
            else -> throw IllegalArgumentException("Unsupported view type: $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            TYPE_HEADER -> {
                (holder as PostsHeaderViewHolder).bind(channelList ?: return)
            }
            TYPE_OTHER -> {
                (holder as PostViewHolder).bind(currentList.orEmpty()[position - 1])
            }
            else -> throw IllegalArgumentException(
                "Unsupported view type: ${getItemViewType(position)}"
            )
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        when (getItemViewType(position)) {
            TYPE_HEADER -> {
                with(holder as PostsHeaderViewHolder) {
                    if (payloads.isEmpty()) {
                        bind(channelList ?: return)
                    } else bind(payloads.first() as PagedList<Channel>)
                }
            }
            TYPE_OTHER -> with(holder as PostViewHolder) {
                if (payloads.isNotEmpty()) {
                    updateFromPayloads(payloads)
                } else bind(currentList.orEmpty()[position - 1])
            }
            else -> throw IllegalArgumentException(
                "Unsupported view type: ${getItemViewType(position)}"
            )
        }
    }

    fun submitChannels(channelList: PagedList<Channel>) {
        this.channelList = channelList
        notifyItemChanged(0, channelList)
    }


    override fun getItemViewType(position: Int): Int {
        return when (position) {
            0 -> TYPE_HEADER
            else -> TYPE_OTHER
        }
    }

    override fun getItemCount(): Int {
        return (currentList?.size ?: 0) + 1
    }

    companion object {
        private const val TYPE_HEADER = 1
        private const val TYPE_OTHER = 2
    }

}