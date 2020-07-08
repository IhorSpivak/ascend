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

    private var recyclerView: RecyclerView? = null
    private var channelList: PagedList<Channel>? = null

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        this.recyclerView = recyclerView
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        this.recyclerView = null
        super.onDetachedFromRecyclerView(recyclerView)
    }

    override fun submitList(list: com.doneit.ascend.domain.use_case.PagedList<Post>) {
        val state = recyclerView?.layoutManager?.onSaveInstanceState()
        super.submitList(list)
        recyclerView?.layoutManager?.onRestoreInstanceState(state)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_HEADER -> PostsHeaderViewHolder.create(parent, user, onPostClickListeners)
            TYPE_OTHER -> PostViewHolder.create(
                parent,
                onPostClickListeners.copy(onLikeClick = { isLiked: Boolean, id: Long ->
                    onPostClickListeners.onLikeClick(isLiked, id)
                    notifyItemChanged(
                        currentList.orEmpty().indexOfFirst { it.id == id },
                        PostViewHolder.buildPayload(!isLiked)
                    )
                })
            )
            else -> throw IllegalArgumentException("Unsupported view type: $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            TYPE_HEADER -> {
                if (channelList == null) {
                    (holder as PostsHeaderViewHolder).bind()
                } else (holder as PostsHeaderViewHolder).bind(channelList ?: return)
            }
            TYPE_OTHER -> currentList.orEmpty()[position].let {
                (holder as PostViewHolder).bind(it)
            }
            else -> throw IllegalArgumentException(
                "Unsupported view type: ${getItemViewType(
                    position
                )}"
            )
        }
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        when (getItemViewType(position)) {
            TYPE_HEADER -> {
                if (channelList == null) {
                    (holder as PostsHeaderViewHolder).bind()
                } else (holder as PostsHeaderViewHolder).bind(channelList ?: return)
            }
            TYPE_OTHER -> currentList.orEmpty()[position].let {
                (holder as PostViewHolder).run {
                    if(payloads.isNotEmpty()) {
                        updateFromPayloads(payloads)
                    } else bind(currentList.orEmpty()[position])
                }
            }
            else -> throw IllegalArgumentException(
                "Unsupported view type: ${getItemViewType(
                    position
                )}"
            )
        }
    }

    fun submitChannels(channelList: PagedList<Channel>) {
        this.channelList = channelList
        (recyclerView?.findViewHolderForAdapterPosition(0) as? PostsHeaderViewHolder)
            ?.setList(channelList)
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