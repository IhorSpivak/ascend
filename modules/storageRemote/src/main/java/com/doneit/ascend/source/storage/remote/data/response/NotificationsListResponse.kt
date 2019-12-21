package com.doneit.ascend.source.storage.remote.data.response

import com.doneit.ascend.source.storage.remote.data.response.base.PagedResponse
import com.google.gson.annotations.SerializedName

class NotificationsListResponse(
    count: Int,
    @SerializedName("notifications") val notifications: List<NotificationResponse>?
) : PagedResponse(count)