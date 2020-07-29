package com.doneit.ascend.presentation.main.home.community_feed.comments_view.common

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.doneit.ascend.domain.entity.community_feed.Comment
import com.doneit.ascend.presentation.common.setOnSingleClickListener
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.databinding.ListItemOtherCommentBinding
import com.doneit.ascend.presentation.utils.extensions.*

class OtherCommentHolder(
    itemView: View,
    private val onUserClick: (userId: Long) -> Unit
) : BaseCommentHolder(itemView) {
    private val binding: ListItemOtherCommentBinding = DataBindingUtil.bind(itemView)!!

    override fun bind(comment: Comment, nextComment: Comment?): Unit = with(binding){
        this.comment = comment
        this.memberEntity = comment.user
        time.apply {
            text = MESSAGE_FORMATTER.toDefaultFormatter().format(comment.createdAt)
            visibleOrGone(
                nextComment == null || calculateDate(
                    comment.createdAt,
                    nextComment.createdAt
                )
            )
            userImage.visible(comment.user.id != nextComment?.user?.id)
            corner.visible(nextComment == null || comment.user.id != nextComment.user.id)
            //TODO: check if we need online status
            isOnline.visible(false)
            userImage.setOnSingleClickListener {
                onUserClick(comment.user.id)
            }
        }
    }

    companion object {
        fun create(parent: ViewGroup,
                   onUserClick: (userId: Long) -> Unit): OtherCommentHolder {
            return OtherCommentHolder(
                DataBindingUtil.inflate<ListItemOtherCommentBinding>(
                    LayoutInflater.from(parent.context),
                    R.layout.list_item_other_comment,
                    parent,
                    false
                ).root,
                onUserClick = onUserClick
            )
        }
    }
}