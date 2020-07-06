package com.doneit.ascend.presentation.main.home.community_feed.common

data class PostClickListeners(
    val onUserClick: (Long) -> Unit,
    val onComplainClick: (Long) -> Unit,
    val onOptionsClick: () -> Unit,
    val onSendCommentClick: (Long, String) -> Unit,
    val onLikeClick: (Long) -> Unit,
    val onShareClick: (Long) -> Unit
)