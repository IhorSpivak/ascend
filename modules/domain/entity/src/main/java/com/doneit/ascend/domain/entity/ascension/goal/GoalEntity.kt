package com.doneit.ascend.domain.entity.ascension.goal

import android.os.Parcelable
import androidx.annotation.StringRes
import com.doneit.ascend.domain.entity.ascension.spiritual_action_step.SpiritualActionStepEntity
import kotlinx.android.parcel.Parcelize

@Parcelize
class GoalEntity(
    val id: Int,
    val type: GoalType,
    @StringRes val name: Int,
    val actionStepList: ArrayList<SpiritualActionStepEntity>,
    val duration: GoalDurationEntity
) : Parcelable {
}