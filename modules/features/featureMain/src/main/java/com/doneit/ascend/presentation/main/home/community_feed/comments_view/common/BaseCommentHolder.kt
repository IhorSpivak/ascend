package com.doneit.ascend.presentation.main.home.community_feed.comments_view.common

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.doneit.ascend.domain.entity.community_feed.Comment

abstract class BaseCommentHolder(
    itemView: View
) : RecyclerView.ViewHolder(itemView) {
    abstract fun bind(comment: Comment, nextComment: Comment?)
}