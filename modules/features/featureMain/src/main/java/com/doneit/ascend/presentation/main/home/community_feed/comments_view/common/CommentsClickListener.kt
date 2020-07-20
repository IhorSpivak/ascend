package com.doneit.ascend.presentation.main.home.community_feed.comments_view.common

import com.doneit.ascend.domain.entity.community_feed.Comment

data class CommentsClickListener(
    val onUserClick: (Long) -> Unit,
    val onDeleteClick: (Comment) -> Unit
)