package com.doneit.ascend.domain.entity.ascension.goal

import android.os.Parcelable
import com.doneit.ascend.domain.entity.ascension.spiritual_action_step.SpiritualActionStepEntity
import kotlinx.android.parcel.Parcelize

@Parcelize
class GoalEntity(
    val id: Int,
    val type: GoalType,
    val name: String,
    val actionStepList: ArrayList<SpiritualActionStepEntity>,
    val duration: GoalDurationEntity
) : Parcelable {
}