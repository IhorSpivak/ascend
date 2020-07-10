package com.doneit.ascend.presentation.main.home.community_feed.comments_view.common

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.doneit.ascend.domain.entity.community_feed.Comment
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.databinding.ListItemOtherMessageBinding

class OtherCommentHolder(
    itemView: View
) : BaseCommentHolder(itemView) {
    private val binding: ListItemOtherMessageBinding = DataBindingUtil.bind(itemView)!!

    override fun bind(comment: Comment) {

    }

    companion object {
        fun create(parent: ViewGroup): OtherCommentHolder {
            return OtherCommentHolder(
                DataBindingUtil.inflate<ListItemOtherMessageBinding>(
                    LayoutInflater.from(parent.context),
                    R.layout.list_item_own_message,
                    parent,
                    false
                ).root
            )
        }
    }
}