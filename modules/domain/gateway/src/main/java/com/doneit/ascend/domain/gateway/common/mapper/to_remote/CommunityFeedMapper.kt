package com.doneit.ascend.domain.gateway.common.mapper.to_remote

import com.doneit.ascend.domain.entity.dto.CommunityFeedDTO
import com.doneit.ascend.source.storage.remote.data.request.community_feed.PostsRequest

fun CommunityFeedDTO.toRequest(page: Int): PostsRequest {
    return PostsRequest(
        page,
        perPage,
        sortColumn,
        sortType?.toString(),
        createdAtFrom,
        createdAtTo,
        updatedAtTo,
        updatedAtFrom
    )
}