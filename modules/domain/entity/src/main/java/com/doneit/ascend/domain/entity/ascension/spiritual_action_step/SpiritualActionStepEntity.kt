package com.doneit.ascend.domain.entity.ascension.spiritual_action_step

import android.os.Parcelable
import com.doneit.ascend.domain.entity.CalendarDayEntity
import kotlinx.android.parcel.Parcelize

@Parcelize
class SpiritualActionStepEntity(
    val id: Int,
    val name: String?,
    val isCompleted: Boolean,
    val timeCommitment: TimeCommitmentEntity,
    val deadline: Long,
    val repeatType: RepeatType,
    val timesCount: RepeatDayEntity?,
    val weekList: RepeatWeekEntity?,
    val monthRange: RepeatMonthEntity?
) : Parcelable{
    @Parcelize
    class RepeatDayEntity(
        var value: Int
    ) : Parcelable

    @Parcelize
    class RepeatWeekEntity(
        var value: ArrayList<CalendarDayEntity>
    ): Parcelable

    @Parcelize
    class RepeatMonthEntity(
        var value: ArrayList<Short>
    ): Parcelable
}