package com.doneit.ascend.presentation.profile.block_list.common

import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import com.doneit.ascend.domain.entity.chats.BlockedUserEntity
import com.doneit.ascend.presentation.main.chats.common.BlockedUserDiffCallback

class BlockedUsersAdapter(
    private val onButtonClick: (member: BlockedUserEntity) -> Unit
): PagedListAdapter<BlockedUserEntity, BlockedUserViewHolder>(BlockedUserDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BlockedUserViewHolder {
        return BlockedUserViewHolder.create(
            parent,
            onButtonClick
        )
    }

    override fun onBindViewHolder(holder: BlockedUserViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it)
        }
    }
}