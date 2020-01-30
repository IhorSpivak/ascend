package com.doneit.ascend.source.storage.remote.data.response.group

import com.doneit.ascend.source.storage.remote.data.response.base.PagedResponse
import com.google.gson.annotations.SerializedName

class ParticipantListResponse(
    count: Int,
    @SerializedName("participants") val participants: List<ParticipantResponse>
) : PagedResponse(count)