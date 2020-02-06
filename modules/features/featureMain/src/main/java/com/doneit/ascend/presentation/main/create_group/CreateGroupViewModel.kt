package com.doneit.ascend.presentation.main.create_group

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.doneit.ascend.domain.entity.CalendarDayEntity
import com.doneit.ascend.domain.entity.MonthEntity
import com.doneit.ascend.domain.entity.getDefaultCalendar
import com.doneit.ascend.domain.use_case.interactor.group.GroupUseCase
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.base.BaseViewModelImpl
import com.doneit.ascend.presentation.main.create_group.calendar_picker.CalendarPickerContract
import com.doneit.ascend.presentation.main.create_group.create_support_group.CreateSupGroupContract
import com.doneit.ascend.presentation.main.create_group.date_picker.DatePickerContract
import com.doneit.ascend.presentation.main.create_group.master_mind.CreateGroupContract
import com.doneit.ascend.presentation.models.*
import com.doneit.ascend.presentation.utils.*
import com.doneit.ascend.presentation.utils.extensions.toDefaultFormatter
import com.doneit.ascend.presentation.utils.extensions.toErrorMessage
import com.vrgsoft.networkmanager.livedata.SingleLiveManager
import kotlinx.coroutines.launch
import java.util.*

class CreateGroupViewModel(
    private val groupUseCase: GroupUseCase,
    private val router: CreateGroupContract.Router,
    private val calendarUtil: CalendarPickerUtil
) : BaseViewModelImpl(),
    CreateGroupContract.ViewModel,
    CreateSupGroupContract.ViewModel,
    CalendarPickerContract.ViewModel,
    DatePickerContract.ViewModel {

    override val createGroupModel = PresentationCreateGroupModel()
    override var email: ValidatableField = ValidatableField()
    override val canComplete = MutableLiveData<Boolean>()
    override val canAddParticipant = MutableLiveData<Boolean>()
    override val canOk = MutableLiveData<Boolean>()

    override val participants = MutableLiveData<List<String>>()
    override val networkErrorMessage = SingleLiveManager<String>()
    override val clearReservationSeat = SingleLiveManager<Boolean>()

    init {
        createGroupModel.name.validator = { s ->
            val result = ValidationResult()

            if (s.isValidGroupName().not()) {
                result.isSucceed = false
                result.errors.add(R.string.error_group_name)
            }

            result
        }

        createGroupModel.startDate.validator = { s ->
            val result = ValidationResult()

            if (s.isValidStrartDate().not()) {
                result.isSucceed = false
                result.errors.add(R.string.error_start_date)
            }

            result
        }

        createGroupModel.meetingFormat.validator = { s ->
            val result = ValidationResult()

            if (createGroupModel.groupType == GroupType.SUPPORT && s.isBlank()) {
                result.isSucceed = false
            }

            result
        }

        createGroupModel.price.validator = { s ->
            val result = ValidationResult()

            if (s.isValidPrice().not()) {
                //will never execute because of edit text mask installed at fragment
                result.isSucceed = false
                result.errors.add(R.string.error_price)
            }

            result
        }

        createGroupModel.numberOfMeetings.validator = { s ->
            val result = ValidationResult()

            if (s.isValidMeetingsNumber().not()) {
                result.isSucceed = false
                result.errors.add(R.string.error_number_of_meetings)
            } else if (createGroupModel.scheduleDays.size > s.toInt()) {
                result.isSucceed = false
                result.errors.add(R.string.error_number_of_meetings_count)
            }

            result
        }

        createGroupModel.tags.validator = { s ->
            val result = ValidationResult()

            if (createGroupModel.groupType == GroupType.SUPPORT && s.isBlank()) {
                result.isSucceed = false
            }

            result
        }

        createGroupModel.description.validator = { s ->
            val result = ValidationResult()

            if (s.isDescriptionValid().not()) {
                result.isSucceed = false
                result.errors.add(R.string.error_description)
            }

            result
        }

        email.validator = { s ->
            val result = ValidationResult()

            if (s.isValidEmail().not()) {
                result.isSucceed = false
                result.errors.add(R.string.error_email)
            }

            result
        }

        createGroupModel.image.validator = { s ->
            val result = ValidationResult()

            if (s.isEmpty()) {
                result.isSucceed = false
            }

            result
        }

        val invalidationListener = { updateCanCreate() }
        createGroupModel.name.onFieldInvalidate = invalidationListener
        createGroupModel.meetingFormat.onFieldInvalidate = invalidationListener
        createGroupModel.numberOfMeetings.onFieldInvalidate = invalidationListener
        createGroupModel.startDate.onFieldInvalidate = invalidationListener
        createGroupModel.price.onFieldInvalidate = invalidationListener
        createGroupModel.tags.onFieldInvalidate = invalidationListener
        createGroupModel.description.onFieldInvalidate = invalidationListener
        createGroupModel.image.onFieldInvalidate = invalidationListener

        email.onFieldInvalidate = { updateCanAddParticipant() }
    }

    override fun addNewParticipant() {
        val newParticipantEmail = email.observableField.get()

        newParticipantEmail?.let {

            val participantsList = participants.value
            val newParticipantList = mutableListOf<String>()

            if (participantsList != null && participantsList.isNotEmpty()) {
                newParticipantList.addAll(participantsList)
            }

            newParticipantList.add(newParticipantEmail)
            participants.postValue(newParticipantList)
            createGroupModel.participants.set(newParticipantList)

            email.observableField.set("")
            clearReservationSeat.call(true)
        }
    }

    override fun completeClick() {
        canComplete.postValue(false)

        viewModelScope.launch {
            val requestEntity =
                groupUseCase.createGroup(createGroupModel.toEntity())

            canComplete.postValue(true)

            if (requestEntity.isSuccessful) {
                router.onBack()
                router.onBack()
            } else {
                if (requestEntity.errorModel!!.isNotEmpty()) {
                    showDefaultErrorMessage(requestEntity.errorModel!!.toErrorMessage())
                }
            }
        }
    }


    override fun backClick() {
        router.onBack()
    }

    override fun chooseScheduleTouch() {
        updateCanOk()
        router.navigateToCalendarPiker()
    }

    override fun chooseStartDateTouch() {
        router.navigateToDatePicker()
    }

    override fun okClick() {
        changeSchedule()
        router.onBack()
    }

    override fun setHours(hours: String) {
        createGroupModel.hours = hours
    }

    override fun getHoursPosition(): Int {
        return createGroupModel.hoursPosition
    }

    override fun setHoursPosition(position: Int) {
        createGroupModel.hoursPosition = position
    }

    override fun setMinutes(minutes: String) {
        createGroupModel.minutes = minutes
    }

    override fun getMinutesPosition(): Int {
        return createGroupModel.minutesPosition
    }

    override fun setMinutesPosition(position: Int) {
        createGroupModel.minutesPosition = position
    }

    override fun setTimeType(timeType: String) {
        createGroupModel.timeType = timeType
    }

    override fun getTimeType(): String {
        return createGroupModel.timeType
    }

    override fun getTimeTypePosition(): Int {
        return createGroupModel.timeTypePosition
    }

    override fun setTimeTypePosition(position: Int) {
        createGroupModel.timeTypePosition = position
    }

    override fun changeDayState(day: CalendarDayEntity, state: Boolean) {
        if (state) {
            createGroupModel.selectedDays.add(day)
        } else {
            createGroupModel.selectedDays.removeAll { it == day }
        }

        updateCanOk()
    }

    override fun applyArguments(args: CreateGroupArgs) {
        createGroupModel.groupType = args.groupType
    }

    override fun onClickRemove(value: String) {
        val participantsList = participants.value
        val newParticipantList = mutableListOf<String>()

        if (participantsList != null && participantsList.isNotEmpty()) {
            newParticipantList.addAll(participantsList.filter { s -> s != value })
        }

        participants.postValue(newParticipantList)
        createGroupModel.participants.set(newParticipantList)
    }

    private fun updateCanOk() {
        val confirmBtnState =
            createGroupModel.selectedDays.size != 0 || createGroupModel.getStartTimeDay() != null

        canOk.postValue(confirmBtnState)
    }

    private fun updateCanCreate() {
        var isFormValid = true

        isFormValid = isFormValid and createGroupModel.name.isValid
        isFormValid = isFormValid and (createGroupModel.meetingFormat.isValid
                || createGroupModel.groupType != GroupType.SUPPORT)
        isFormValid = isFormValid and
                createGroupModel.scheduleTime.observableField.getNotNull().isNotEmpty() and
                createGroupModel.scheduleDays.isNotEmpty()
        isFormValid = isFormValid and createGroupModel.numberOfMeetings.isValid
        isFormValid = isFormValid and createGroupModel.startDate.isValid
        isFormValid = isFormValid and (createGroupModel.price.isValid
                || createGroupModel.groupType == GroupType.SUPPORT)
        isFormValid = isFormValid and (createGroupModel.tags.isValid
                || createGroupModel.groupType != GroupType.SUPPORT)
        isFormValid = isFormValid and createGroupModel.description.isValid
        isFormValid = isFormValid and createGroupModel.image.isValid

        canComplete.postValue(isFormValid)
    }

    private fun updateCanAddParticipant() {
        var isFormValid = true

        isFormValid = isFormValid and email.isValid

        canAddParticipant.postValue(isFormValid)
    }

    private fun changeSchedule() {

        val builder = StringBuilder()

        val days = createGroupModel.selectedDays.map { it }.toMutableList()
        createGroupModel.getStartTimeDay()?.let {
            if (days.contains(it).not()) {
                days.add(it)
            }
        }
        days.sortBy { it.ordinal }


        days.forEach {
            builder.append("${calendarUtil.getString(it)}, ")
        }
        builder.deleteCharAt(builder.length - 1)//remove space
        builder.deleteCharAt(builder.length - 1)//remove coma

        builder.append("\n${createGroupModel.hours}:${createGroupModel.minutes} ${createGroupModel.timeType.toLowerCase()}")
        createGroupModel.scheduleTime.observableField.set(builder.toString())

        createGroupModel.scheduleDays.clear()
        createGroupModel.scheduleDays.addAll(days)
    }

    private fun changeStartDate() {
        val calendar = getDefaultCalendar()
        calendar.time = Date(0)
        calendar.set(Calendar.YEAR, createGroupModel.year)
        calendar.set(Calendar.MONTH, createGroupModel.month.ordinal)
        calendar.set(Calendar.DAY_OF_MONTH, createGroupModel.day)

        createGroupModel.startDate.observableField.set(START_TIME_FORMATTER.format(calendar.time))
        changeSchedule()
    }

    override fun getMonthList(): List<MonthEntity> {
        val monthList = mutableListOf<MonthEntity>()
        val commonMonthList = MonthEntity.values()
        val actualMonth = createGroupModel.month.ordinal

        for (i in 0..11) {
            val totalIndex = (i + actualMonth) % commonMonthList.size
            monthList.add(commonMonthList[totalIndex])
        }

        return monthList
    }

    override fun cancelClick() {
        router.onBack()

        canOk.postValue(false)
    }

    override fun doneClick() {
        changeStartDate()
        router.onBack()
    }

    override fun backDateClick() {
        router.onBack()
        router.onBack()
    }

    override fun getMonth(): MonthEntity {
        return createGroupModel.month
    }

    override fun setMonth(month: MonthEntity) {
        createGroupModel.month = month
    }

    override fun setDay(day: Int) {
        createGroupModel.day = day
    }

    override fun setDayPosition(position: Int) {
        createGroupModel.dayPosition = position
    }

    override fun getDayPosition(): Int {
        return createGroupModel.dayPosition
    }

    override fun setYear(year: Int) {
        createGroupModel.year = year
    }

    override fun getYear(): Int {
        return createGroupModel.year
    }

    override fun setYearPosition(position: Int) {
        createGroupModel.yearPosition = position
    }

    override fun getYearPosition(): Int {
        return createGroupModel.yearPosition
    }

    companion object {
        val START_TIME_FORMATTER = "dd MMMM yyyy".toDefaultFormatter()
    }
}