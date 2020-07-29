package com.doneit.ascend.presentation.main.home.community_feed.comments_view.common

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.doneit.ascend.domain.entity.community_feed.Comment
import com.doneit.ascend.domain.entity.user.UserEntity
import com.doneit.ascend.presentation.common.PaginationAdapter

class CommentsAdapter(
    private val user: UserEntity,
    private val commentsClickListener: CommentsClickListener,
    override val doAfterListUpdated: () -> Unit = {}
) : PaginationAdapter<Comment, RecyclerView.ViewHolder>(CommentItemCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VT_OWN_MESSAGE -> OwnCommentHolder.create(parent, commentsClickListener.onDeleteClick)
            VT_OTHER_MESSAGE -> OtherCommentHolder.create(parent, commentsClickListener.onUserClick)
            else -> throw IllegalStateException("Unknown type $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val nextMessage = if (position != itemCount - 1) getItem(position + 1) else null
        when (getItemViewType(position)) {
            VT_OWN_MESSAGE -> {
                (holder as OwnCommentHolder).bind(getItem(position), nextMessage)
            }
            VT_OTHER_MESSAGE -> {
                (holder as OtherCommentHolder).bind(getItem(position), nextMessage)
            }
            else -> throw IllegalArgumentException(
                "Unsupported view type: ${getItemViewType(position)}"
            )
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position).user.id) {
            user.id -> VT_OWN_MESSAGE
            else -> VT_OTHER_MESSAGE
        }
    }

    companion object {
        const val VT_OWN_MESSAGE = 1
        const val VT_OTHER_MESSAGE = 2
    }
}