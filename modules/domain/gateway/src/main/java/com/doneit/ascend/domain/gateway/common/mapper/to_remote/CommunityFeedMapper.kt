package com.doneit.ascend.domain.gateway.common.mapper.to_remote

import com.doneit.ascend.domain.entity.dto.CommentsDTO
import com.doneit.ascend.domain.entity.dto.CommunityFeedDTO
import com.doneit.ascend.domain.entity.dto.ShareDTO
import com.doneit.ascend.source.storage.remote.data.request.community_feed.CommentsRequest
import com.doneit.ascend.source.storage.remote.data.request.community_feed.PostsRequest
import com.doneit.ascend.source.storage.remote.data.request.community_feed.ShareRequest

fun CommunityFeedDTO.toRequest(page: Int): PostsRequest {
    return PostsRequest(
        page = page,
        perPage = perPage,
        sortColumn = sortColumn,
        sortType = sortType?.toString(),
        createdAtFrom = createdAtFrom,
        createdAtTo = createdAtTo,
        updatedAtFrom = updatedAtFrom,
        user_id = user_id,
        community = community,
        updatedAtTo = updatedAtTo

    )
}

fun CommentsDTO.toRequest(page: Int): CommentsRequest {
    return CommentsRequest(
        page = page,
        perPage = perPage,
        sortColumn = sortColumn,
        sortType = sortType?.toString(),
        createdAtFrom = createdAtFrom,
        createdAtTo = createdAtTo,
        updatedAtFrom = updatedAtFrom,
        updatedAtTo = updatedAtTo,
        text = text
    )
}

fun ShareDTO.toRequest(): ShareRequest {
    return ShareRequest(
        chatIds = chatIds,
        userIds = userIds
    )
}