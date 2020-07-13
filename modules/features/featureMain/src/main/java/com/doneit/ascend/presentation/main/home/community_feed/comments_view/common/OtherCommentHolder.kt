package com.doneit.ascend.presentation.main.home.community_feed.comments_view.common

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.doneit.ascend.domain.entity.community_feed.Comment
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.databinding.ListItemOtherCommentBinding
import com.doneit.ascend.presentation.utils.extensions.MESSAGE_FORMATTER
import com.doneit.ascend.presentation.utils.extensions.calculateDate
import com.doneit.ascend.presentation.utils.extensions.toDefaultFormatter
import com.doneit.ascend.presentation.utils.extensions.visibleOrGone

class OtherCommentHolder(
    itemView: View
) : BaseCommentHolder(itemView) {
    private val binding: ListItemOtherCommentBinding = DataBindingUtil.bind(itemView)!!

    override fun bind(comment: Comment, nextComment: Comment?): Unit = with(binding){
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
    }

    companion object {
        fun create(parent: ViewGroup): OtherCommentHolder {
            return OtherCommentHolder(
                DataBindingUtil.inflate<ListItemOtherCommentBinding>(
                    LayoutInflater.from(parent.context),
                    R.layout.list_item_other_comment,
                    parent,
                    false
                ).root
            )
        }
    }
}