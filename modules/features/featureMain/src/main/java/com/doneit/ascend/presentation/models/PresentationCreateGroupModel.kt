package com.doneit.ascend.presentation.models

import androidx.databinding.ObservableField
import com.doneit.ascend.domain.entity.CalendarDayEntity
import com.doneit.ascend.domain.entity.MonthEntity
import com.doneit.ascend.presentation.main.create_group.master_mind.CreateGroupViewModel
import com.doneit.ascend.presentation.utils.CalendarPickerUtil
import com.doneit.ascend.presentation.utils.extensions.toCalendar
import java.util.*

class PresentationCreateGroupModel(
    var name: ValidatableField = ValidatableField(),
    var numberOfMeetings: ValidatableField = ValidatableField(),
    var startDate: ValidatableField = ValidatableField(),
    var price: ValidatableField = ValidatableField(),
    var description: ValidatableField = ValidatableField(),
    var image: ValidatableField = ValidatableField(),
    var participants: ObservableField<List<String>> = ObservableField(),
    var meetingFormat: ValidatableField = ValidatableField(),
    var tags: ValidatableField = ValidatableField(),
    var isPublic: ObservableField<Boolean> = ObservableField(true),
    var groupType: GroupType? = null,
    var hours: String = "00",
    var hoursPosition: Int = 0,
    var minutes: String = "00",
    var minutesPosition: Int = 0,
    var timeType: String = CalendarPickerUtil.DEFAULT_TIME_TYPE,
    var timeTypePosition: Int = 0,
    var month: MonthEntity = MonthEntity.getActual(),
    var day: Int = 0,
    var dayPosition: Int = -1,
    var year: Int = 0,
    var yearPosition: Int = 0,
    val selectedDays: MutableList<CalendarDayEntity> = mutableListOf(),
    val scheduleDays: MutableList<CalendarDayEntity> = mutableListOf(),
    val scheduleTime: ValidatableField = ValidatableField()
) {
    fun getStartTimeDay(): CalendarDayEntity? {
        var result: CalendarDayEntity? = null

        val start = startDate.observableField.get()
        if(start.isNullOrEmpty().not()) {
            @Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
            val date = CreateGroupViewModel.START_TIME_FORMATTER.parse(start).toCalendar()
            val dayIndex = date.get(Calendar.DAY_OF_WEEK).toOrdinal()

            result = CalendarDayEntity.values()[dayIndex]
        }

        return result
    }

    private fun Int.toOrdinal(): Int {
        return (this + 6) % 7
    }
}

enum class GroupType {
    WEBINAR,
    SUPPORT,
    MASTER_MIND;

    override fun toString(): String {
        return super.toString().toLowerCase()
    }
}
