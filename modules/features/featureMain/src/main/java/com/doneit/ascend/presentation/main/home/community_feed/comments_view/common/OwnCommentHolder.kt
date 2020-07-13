package com.doneit.ascend.presentation.main.home.community_feed.comments_view.common

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.doneit.ascend.domain.entity.community_feed.Comment
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.databinding.ListItemOwnCommentBinding
import com.doneit.ascend.presentation.utils.extensions.MESSAGE_FORMATTER
import com.doneit.ascend.presentation.utils.extensions.calculateDate
import com.doneit.ascend.presentation.utils.extensions.toDefaultFormatter
import com.doneit.ascend.presentation.utils.extensions.visibleOrGone

class OwnCommentHolder private constructor(
    itemView: View,
    private val onDeleteClick: (comment: Comment) -> Unit
) : BaseCommentHolder(itemView) {

    private val binding: ListItemOwnCommentBinding = DataBindingUtil.bind(itemView)!!

    override fun bind(comment: Comment, nextComment: Comment?) = with(binding) {
        this.comment = comment
        time.apply {
            text = MESSAGE_FORMATTER.toDefaultFormatter().format(comment.createdAt)
            visibleOrGone(
                nextComment == null || calculateDate(
                    comment.createdAt,
                    nextComment.createdAt
                )
            )
        }

        ibDelete.setOnClickListener {
            onDeleteClick(comment)
        }
    }

    companion object {
        fun create(
            parent: ViewGroup,
            onDeleteClick: (comment: Comment) -> Unit
        ): OwnCommentHolder {
            return OwnCommentHolder(
                DataBindingUtil.inflate<ListItemOwnCommentBinding>(
                    LayoutInflater.from(parent.context),
                    R.layout.list_item_own_comment,
                    parent,
                    false
                ).root,
                onDeleteClick = onDeleteClick
            )
        }
    }
}