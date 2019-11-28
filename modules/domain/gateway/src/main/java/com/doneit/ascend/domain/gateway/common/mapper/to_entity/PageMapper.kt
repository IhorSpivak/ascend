package com.doneit.ascend.domain.gateway.common.mapper.to_entity

import com.doneit.ascend.domain.entity.PageEntity
import com.doneit.ascend.source.storage.remote.data.response.PageResponse

fun PageResponse.toEntity(): PageEntity {
    return PageEntity(
        pageType,
        content,
        createdAt,
        updatedAt,
        errors
    )
}