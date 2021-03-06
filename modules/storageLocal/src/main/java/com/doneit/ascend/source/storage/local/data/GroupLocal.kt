package com.doneit.ascend.source.storage.local.data

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "groups")
data class GroupLocal(
    @PrimaryKey val id: Long,
    val name: String?,
    val description: String?,
    @ColumnInfo(name = "start_time") val startTime: String?,
    val status: Int?,
    @ColumnInfo(name = "group_type") val groupType: Int?,
    val price: Float?,
    @Embedded(prefix = "img") val image: ImageLocal?,
    @ColumnInfo(name = "meetings_count") val meetingsCount: Int?,
    @ColumnInfo(name = "past_meetings_count") val pastMeetingsCount: Int?,
    @ColumnInfo(name = "passed_count") val passedCount: Int,
    @ColumnInfo(name = "created_at") val createdAt: String?,
    @ColumnInfo(name = "updated_at") val updatedAt: String?,
    @Embedded(prefix = "owner") val owner: OwnerLocal?,
    @Embedded(prefix = "banner") val banner: BannerLocal?,
    val subscribed: Boolean?,
    val invited: Boolean?,
    val blocked: Boolean?,
    @ColumnInfo(name = "participants_count") val participantsCount: Int?,
    val invitesCount: Int?,
    @ColumnInfo(name = "days_of_week") var daysOfWeek: List<Int>,
    @Embedded(prefix = "notes") val note: NoteLocal?,
    @ColumnInfo(name = "meeting_format") val meetingFormat: String?,
    @Embedded(prefix = "tag") val tag: TagLocal?,
    val isPrivate: Boolean,
    @ColumnInfo(name = "dates") var dates: List<String>?,
    @ColumnInfo(name = "themes") var themes: List<String>?,
    val duration: Int,
    val community: String?
)