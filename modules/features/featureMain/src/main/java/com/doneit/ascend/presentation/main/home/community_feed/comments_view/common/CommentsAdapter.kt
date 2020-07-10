package com.doneit.ascend.presentation.main.home.community_feed.comments_view.common

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.doneit.ascend.domain.entity.community_feed.Comment
import com.doneit.ascend.domain.entity.user.UserEntity
import com.doneit.ascend.presentation.common.PaginationAdapter

class CommentsAdapter(
    private val user: UserEntity
) : PaginationAdapter<Comment, RecyclerView.ViewHolder>(CommentItemCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VT_OWN_MESSAGE -> OwnCommentHolder.create(parent)
            VT_OTHER_MESSAGE -> OtherCommentHolder.create(parent)
            else -> throw IllegalStateException("Unknown type $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
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