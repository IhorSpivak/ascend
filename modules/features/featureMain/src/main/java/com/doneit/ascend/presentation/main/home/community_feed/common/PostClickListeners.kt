package com.doneit.ascend.presentation.main.home.community_feed.common

import android.view.View
import com.doneit.ascend.domain.entity.chats.ChatEntity
import com.doneit.ascend.domain.entity.community_feed.Attachment
import com.doneit.ascend.domain.entity.community_feed.Post

data class PostClickListeners(
    val onUserClick: (Long) -> Unit,
    val onComplainClick: (Long) -> Unit,
    val onOptionsClick: (View, Post) -> Unit,
    val onSendCommentClick: (Long, String, Int) -> Unit,
    val onLikeClick: (Boolean, Long, Int) -> Unit,
    val onShareClick: (Long) -> Unit,
    val onCreatePostListener: () -> Unit,
    val onSeeAllClickListener: () -> Unit,
    val onChannelClick: (ChatEntity) -> Unit,
    val onCommentClick: (Long) -> Unit,
    val onMediaClick: (List<Attachment>, Int) -> Unit
)