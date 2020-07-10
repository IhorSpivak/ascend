package com.doneit.ascend.presentation.main.home.community_feed.comments_view.common

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.doneit.ascend.domain.entity.community_feed.Comment
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.databinding.ListItemOwnMessageBinding

class OwnCommentHolder private constructor(
    itemView: View,
    private val onDeleteClick: (comment: Comment) -> Unit
) : BaseCommentHolder(itemView) {

    private val binding: ListItemOwnMessageBinding = DataBindingUtil.bind(itemView)!!

    override fun bind(comment: Comment) {
        binding.ibDelete.setOnClickListener {
            onDeleteClick(comment)
        }
    }

    companion object {
        fun create(
            parent: ViewGroup,
            onDeleteClick: (comment: Comment) -> Unit
        ): OwnCommentHolder {
            return OwnCommentHolder(
                DataBindingUtil.inflate<ListItemOwnMessageBinding>(
                    LayoutInflater.from(parent.context),
                    R.layout.list_item_own_message,
                    parent,
                    false
                ).root,
                onDeleteClick = onDeleteClick
            )
        }
    }
}