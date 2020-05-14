package com.doneit.ascend.presentation.models

import androidx.databinding.ObservableField
import com.doneit.ascend.domain.entity.CalendarDayEntity
import com.doneit.ascend.domain.entity.MonthEntity
import com.doneit.ascend.domain.entity.getDefaultCalendar
import com.doneit.ascend.presentation.main.create_group.CreateGroupViewModel
import com.doneit.ascend.presentation.utils.CalendarPickerUtil
import com.doneit.ascend.presentation.utils.extensions.toAmPm
import com.doneit.ascend.presentation.utils.extensions.toCalendar
import com.doneit.ascend.presentation.utils.extensions.toMonthEntity
import com.doneit.ascend.presentation.utils.extensions.toTimeString
import java.util.*

class PresentationCreateGroupModel(
    var name: ValidatableField = ValidatableField(),
    var numberOfMeetings: ValidatableField = ValidatableField(),
    var startDate: ValidatableField = ValidatableField(),
    var price: ValidatableField = ValidatableField(),
    var description: ValidatableField = ValidatableField(),
    var image: ValidatableField = ValidatableField(),
    var participants: ObservableField<List<String>> = ObservableField(),
    var participantsToDelete: ObservableField<List<String>> = ObservableField(),
    var meetingFormat: ValidatableField = ValidatableField(),
    var tags: Int = 0,
    var isPrivate: ObservableField<Boolean> = ObservableField(false),
    var groupType: GroupType? = null,
    var hours: String = "00",
    var hoursOfDay: String = "00",
    var minutes: String = "00",
    var timeType: String = CalendarPickerUtil.DEFAULT_TIME_TYPE,
    var month: MonthEntity = MonthEntity.JANUARY,
    var day: Int = 0,
    var year: Int = 0,
    val selectedDays: MutableList<CalendarDayEntity> = mutableListOf(),
    val scheduleDays: MutableList<CalendarDayEntity> = mutableListOf(),
    val scheduleTime: ValidatableField = ValidatableField(),
    var duration: ValidatableField = ValidatableField(),
    var themesOfMeeting: MutableList<ValidatableField> = mutableListOf(),
    var webinarSchedule: MutableList<ValidatableField> = mutableListOf(ValidatableField()),
    var timeList: MutableList<Calendar> = mutableListOf(getDefaultCalendar()),
    var actualStartTime: Calendar = getDefaultCalendar(),
    var weekDays: MutableList<Int> = mutableListOf()
) {
    init {
        val currentDate = getDefaultCalendar()
        hours = currentDate.get(Calendar.HOUR).toTimeString()
        hoursOfDay = currentDate.get(Calendar.HOUR_OF_DAY).toTimeString()
        minutes = currentDate.get(Calendar.MINUTE).toTimeString()
        timeType = currentDate.get(Calendar.AM_PM).toAmPm()

        day = currentDate.get(Calendar.DAY_OF_MONTH)
        month = currentDate.get(Calendar.MONTH).toMonthEntity()
        year = currentDate.get(Calendar.YEAR)
    }

    fun getStartTimeDay(): CalendarDayEntity? {
        var result: CalendarDayEntity? = null

        val start = startDate.observableField.get()
        if (start.isNullOrEmpty().not()) {
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
    MASTER_MIND,
    INDIVIDUAL;

    override fun toString(): String {
        return super.toString().toLowerCase()
    }
}
