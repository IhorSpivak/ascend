package com.doneit.ascend.domain.gateway.common.mapper.to_remote

import com.doneit.ascend.domain.entity.dto.CommentsDTO
import com.doneit.ascend.domain.entity.dto.CommunityFeedDTO
import com.doneit.ascend.domain.entity.dto.SharePostDTO
import com.doneit.ascend.source.storage.remote.data.request.community_feed.CommentsRequest
import com.doneit.ascend.source.storage.remote.data.request.community_feed.PostsRequest
import com.doneit.ascend.source.storage.remote.data.request.community_feed.SharePostRequest

fun CommunityFeedDTO.toRequest(page: Int): PostsRequest {
    return PostsRequest(
        page = page,
        perPage = perPage,
        sortColumn = sortColumn,
        sortType = sortType?.toString(),
        createdAtFrom = createdAtFrom,
        createdAtTo = createdAtTo,
        updatedAtFrom = updatedAtFrom,
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

fun SharePostDTO.toRequest(): SharePostRequest {
    return SharePostRequest(
        chatIds = chatIds,
        userIds = userIds
    )
}