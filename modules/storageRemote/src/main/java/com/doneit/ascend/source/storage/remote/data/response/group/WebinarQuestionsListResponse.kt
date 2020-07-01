package com.doneit.ascend.source.storage.remote.data.response.group

import com.doneit.ascend.source.storage.remote.data.response.base.PagedResponse
import com.google.gson.annotations.SerializedName

class WebinarQuestionsListResponse(
    count: Int,
    @SerializedName("questions") val questions: List<WebinarQuestionResponse>
) : PagedResponse(count)