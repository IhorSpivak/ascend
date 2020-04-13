package com.doneit.ascend.source.storage.remote.data.response.group

import com.doneit.ascend.source.storage.remote.data.response.ImageResponse
import com.doneit.ascend.source.storage.remote.data.response.OwnerResponse
import com.doneit.ascend.source.storage.remote.data.response.TagResponse
import com.doneit.ascend.source.storage.remote.data.response.user.SearchUsersResponse
import com.google.gson.annotations.SerializedName
import java.util.*

data class GroupResponse(
    @SerializedName("id") val id: Long,
    @SerializedName("name") val name: String,
    @SerializedName("description") val description: String,
    @SerializedName("start_time") val startTime: String,
    @SerializedName("status") val status: String,
    @SerializedName("group_type") val groupType: String,
    @SerializedName("price") val price: Float,
    @SerializedName("image") val image: ImageResponse,
    @SerializedName("meetings_count") val meetingsCount: Int,
    @SerializedName("tag") val tag: TagResponse?,
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("updated_at") val updatedAt: String,
    @SerializedName("owner") val owner: OwnerResponse,
    @SerializedName("subscribed") val subscribed: Boolean,
    @SerializedName("invited") val invited: Boolean,
    @SerializedName("blocked") val blocked: Boolean,
    @SerializedName("participants_count") val participantsCount: Int,
    @SerializedName("invites_count") val invitesCount: Int,
    @SerializedName("wdays") var daysOfWeek: List<Int>? = emptyList(),
    @SerializedName("note") val note: NoteResponse?,
    @SerializedName("meeting_format") val meetingFormat: String?,
    @SerializedName("invites") val invites: List<SearchUsersResponse>?,
    @SerializedName("private") val private: Boolean,
    @SerializedName("past_meetings_count") val pastMeetingsCount: Int,
    @SerializedName("times") val dates: List<String>?,
    @SerializedName("themes") val themes: List<String>?
)