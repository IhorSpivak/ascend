package com.doneit.ascend.presentation.main.create_group

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.doneit.ascend.domain.use_case.interactor.group.GroupUseCase
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.base.BaseViewModelImpl
import com.doneit.ascend.presentation.main.calendar_picker.CalendarPickerContract
import com.doneit.ascend.presentation.main.date_picker.DatePickerContract
import com.doneit.ascend.presentation.main.models.PresentationCreateGroupModel
import com.doneit.ascend.presentation.main.models.ValidatableField
import com.doneit.ascend.presentation.main.models.ValidationResult
import com.doneit.ascend.presentation.main.models.toEntity
import com.doneit.ascend.presentation.utils.*
import com.doneit.ascend.presentation.utils.extensions.toErrorMessage
import com.vrgsoft.annotations.CreateFactory
import com.vrgsoft.annotations.ViewModelDiModule
import com.vrgsoft.networkmanager.livedata.SingleLiveManager
import kotlinx.coroutines.launch

@CreateFactory
@ViewModelDiModule
class CreateGroupViewModel(
    private val groupUseCase: GroupUseCase,
    private val router: CreateGroupContract.Router,
    private val calendarUtil: CalendarPickerUtil,
    private val datePickerUtil: DatePickerUtil
) : BaseViewModelImpl(),
    CreateGroupContract.ViewModel,
    CalendarPickerContract.ViewModel,
    DatePickerContract.ViewModel {

    override val createGroupModel = PresentationCreateGroupModel()
    override var email: ValidatableField = ValidatableField()
    override val canComplete = MutableLiveData<Boolean>()
    override val canAddParticipant = MutableLiveData<Boolean>()
    override val canOk = MutableLiveData<Boolean>()

    override val participants = MutableLiveData<List<String>>()
    override val networkErrorMessage = SingleLiveManager<String>()

    init {
        createGroupModel.name.validator = { s ->
            val result = ValidationResult()

            if (s.isValidGroupName().not()) {
                result.isSussed = false
                result.errors.add(R.string.error_group_name)
            }

            result
        }

        createGroupModel.numberOfMeetings.validator = { s ->
            val result = ValidationResult()

            if (s.isValid4Number().not()) {
                result.isSussed = false
                result.errors.add(R.string.error_number_of_meetings)
            }

            result
        }

//        createGroupModel.startDate.validator = { s ->
//            val result = ValidationResult()
//
//            if (s.isValidStartDate().not()) {
//                result.isSussed = false
//                result.errors.add(R.string.error_start_date)
//            }
//
//            result
//        }

        createGroupModel.price.validator = { s ->
            val result = ValidationResult()

            if (s.isValid4Number().not()) {
                result.isSussed = false
                result.errors.add(R.string.error_price)
            }

            result
        }

        createGroupModel.description.validator = { s ->
            val result = ValidationResult()

            if (s.isDescriptionValid().not()) {
                result.isSussed = false
                result.errors.add(R.string.error_description)
            }

            result
        }

        email.validator = { s ->
            val result = ValidationResult()

            if (s.isValidEmail().not()) {
                result.isSussed = false
                result.errors.add(R.string.error_email)
            }

            result
        }

        createGroupModel.image.validator = { s ->
            val result = ValidationResult()

            if (s.isEmpty()) {
                result.isSussed = false
            }

            result
        }

        val invalidationListener = { updateCanCreate() }
        createGroupModel.name.onFieldInvalidate = invalidationListener
//        createGroupModel.schedule.onFieldInvalidate = invalidationListener
        createGroupModel.numberOfMeetings.onFieldInvalidate = invalidationListener
//        createGroupModel.startDate.onFieldInvalidate = invalidationListener
        createGroupModel.price.onFieldInvalidate = invalidationListener
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
        }
    }

    override fun completeClick() {
        canComplete.postValue(false)

        viewModelScope.launch {
            val requestEntity =
                groupUseCase.createGroup(createGroupModel.toEntity(createGroupModel.groupType))

            canComplete.postValue(true)

            if (requestEntity.isSuccessful) {
                router.closeActivity()
            }
            else {
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

    override fun changeDayState(day: CalendarDay, state: Boolean) {
        if (state) {
            createGroupModel.selectedDays.add(day)
        } else {
            val item = createGroupModel.selectedDays.find { p -> p == day }

            item?.let {
                createGroupModel.selectedDays.remove(it)
            }
        }

        canOk.postValue(createGroupModel.selectedDays.size != 0)
    }

    override fun getSelectedDay(): List<CalendarDay> {
        return createGroupModel.scheduleDays
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

    private fun updateCanCreate() {
        var isFormValid = true

        isFormValid = isFormValid and createGroupModel.name.isValid
        // TODO: uncommite when calendar picker is ready
//        isFormValid = isFormValid and createGroupModel.schedule.isValid
        isFormValid = isFormValid and createGroupModel.numberOfMeetings.isValid
//        isFormValid = isFormValid and createGroupModel.startDate.isValid
        isFormValid = isFormValid and createGroupModel.price.isValid
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

        createGroupModel.selectedDays.sortBy { it.ordinal }
        for ((index, value) in createGroupModel.selectedDays.iterator().withIndex()) {
            if (index != createGroupModel.selectedDays.size - 1) {
                builder.append("${calendarUtil.getString(value)}, ")
            } else {
                builder.append("${calendarUtil.getString(value)} ")
            }
        }

        builder.append("${createGroupModel.hours}:${createGroupModel.minutes} ${createGroupModel.timeType.toLowerCase()}")
        createGroupModel.scheduleTime.observableField.set(builder.toString())

        createGroupModel.scheduleDays.clear()
        createGroupModel.scheduleDays.addAll(createGroupModel.selectedDays)
        createGroupModel.selectedDays.clear()
    }

    private fun changeStartDate() {
        createGroupModel.startDate.observableField.set(
            "${createGroupModel.day} ${datePickerUtil.getStringValue(
                createGroupModel.month
            )} ${createGroupModel.year}"
        )
    }

    override fun cancelClick() {
        router.onBack()

        canOk.postValue(false)
    }

    override fun doneClick() {
        changeStartDate()
        router.onBack()
    }

    override fun setMonth(month: Int) {
        createGroupModel.month = month
    }

    override fun getMonth(): Int {
        return createGroupModel.month
    }

    override fun setMonthPosition(position: Int) {
        createGroupModel.monthPosition = position
    }

    override fun getMonthPosition(): Int {
        return createGroupModel.monthPosition
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
}