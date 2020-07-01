package com.doneit.ascend.domain.gateway.common.mapper.to_remote

import com.doneit.ascend.domain.entity.dto.WebinarQuestionDTO
import com.doneit.ascend.source.storage.remote.data.request.WebinarQuestionsListRequest

fun WebinarQuestionDTO.toRequest(page: Int) : WebinarQuestionsListRequest{
    return WebinarQuestionsListRequest(
        page,
        perPage,
        sortColumn,
        sortType?.toString(),
        content,
        userId,
        fullName,
        createdAtFrom,
        createdAtTo,
        updatedAtTo,
        updatedAtFrom
    )
}