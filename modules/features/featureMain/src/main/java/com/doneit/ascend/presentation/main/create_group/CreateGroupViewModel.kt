package com.doneit.ascend.presentation.main.create_group

import android.icu.text.TimeZoneFormat
import android.widget.EditText
import androidx.lifecycle.*
import androidx.paging.PagedList
import com.doneit.ascend.domain.entity.*
import com.doneit.ascend.domain.entity.group.GroupEntity
import com.doneit.ascend.domain.use_case.interactor.group.GroupUseCase
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.base.BaseViewModelImpl
import com.doneit.ascend.presentation.main.create_group.master_mind.CreateMMGroupContract
import com.doneit.ascend.presentation.models.GroupType
import com.doneit.ascend.presentation.models.PresentationCreateGroupModel
import com.doneit.ascend.presentation.models.ValidatableField
import com.doneit.ascend.presentation.models.ValidationResult
import com.doneit.ascend.presentation.models.group.toEntity
import com.doneit.ascend.presentation.utils.*
import com.doneit.ascend.presentation.utils.extensions.toDefaultFormatter
import com.doneit.ascend.presentation.utils.extensions.toErrorMessage
import com.vrgsoft.networkmanager.livedata.SingleLiveManager
import kotlinx.coroutines.launch
import java.util.*

class CreateGroupViewModel(
    private val groupUseCase: GroupUseCase,
    private val router: CreateGroupHostContract.Router,
    private val localRouter: CreateGroupHostContract.LocalRouter,
    private val calendarUtil: CalendarPickerUtil
) : BaseViewModelImpl(), CreateGroupHostContract.ViewModel {

    override val navigation =
        MutableLiveData<CreateMMGroupContract.Navigation>(CreateMMGroupContract.Navigation.TO_GROUP)
    override val createGroupModel = PresentationCreateGroupModel()
    override var email: ValidatableField = ValidatableField()
    override val canComplete = MutableLiveData<Boolean>()
    override val canAddParticipant = MutableLiveData<Boolean>()
    override val canAddMembers = MutableLiveData<Boolean>()
    override val canOk = MutableLiveData<Boolean>()

    override val participants = MutableLiveData<List<String>>()
    override val users = MutableLiveData<List<ParticipantEntity>>()
    override val networkErrorMessage = SingleLiveManager<String>()
    override val clearReservationSeat = SingleLiveManager<Boolean>()
    override val changeGroup: LiveData<GroupEntity>
        get() = MutableLiveData()

    private val searchQuery = MutableLiveData<String>()

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

        /*createGroupModel.tags.validator = { s ->
            val result = ValidationResult()

            if (createGroupModel.groupType == GroupType.SUPPORT && s.isBlank()) {
                result.isSucceed = false
            }

            result
        }*/

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
        //createGroupModel.tags.onFieldInvalidate = invalidationListener
        createGroupModel.description.onFieldInvalidate = invalidationListener
        createGroupModel.image.onFieldInvalidate = invalidationListener

        //email.onFieldInvalidate = { updateCanAddParticipant() }
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
                router.navigateToDetails(requestEntity.successModel!!)
            } else {
                if (requestEntity.errorModel!!.isNotEmpty()) {
                    showDefaultErrorMessage(requestEntity.errorModel!!.toErrorMessage())
                }
            }
        }
    }

    override fun handleBaseNavigation(args: CreateGroupArgs, group: GroupEntity?, what: String?) {
        when(args.groupType){
            GroupType.SUPPORT -> localRouter.navigateToCreateSupGroup(args, group, what)
            GroupType.MASTER_MIND -> localRouter.navigateToCreateMMGroup(args, group, what)
            GroupType.WEBINAR -> localRouter.navigateToCreateWebinar(args)
            GroupType.INDIVIDUAL -> localRouter.navigateToCreateMMGroup(args, group, what)
        }
    }

    override fun backClick() {
        //todo remove this solution!!
        if (localRouter.onBack().not()) {
            router.onBack()
        }
    }

    override fun chooseScheduleTouch() {
        updateCanOk()
        localRouter.navigateToCalendarPiker()
    }

    override fun chooseStartDateTouch() {
        localRouter.navigateToDatePicker()
    }

    override fun okClick(hours: String, hourOfDay: String, minutes: String, timeType: String) {
        setHours(hours, hourOfDay)
        setMinutes(minutes)
        setTimeType(timeType)
        changeSchedule()
        backClick()
    }

    override fun setHours(hours: String, hourOfDay: String) {
        createGroupModel.hours = hours
        createGroupModel.hoursOfDay = hourOfDay
    }

    override fun setMinutes(minutes: String) {
        createGroupModel.minutes = minutes
    }

    override fun setTimeType(timeType: String) {
        createGroupModel.timeType = timeType
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

        val isValid = when {
            createGroupModel.groupType == GroupType.SUPPORT -> canCreateSupport()
            createGroupModel.isPublic.getNotNull() -> canCreateMMGroup()
            createGroupModel.isPublic.getNotNull().not() -> canCreateMMIndividual()
            else -> canCreateMMIndividual()
        }
        canComplete.postValue(isValid)
    }

    private fun canCreateSupport(): Boolean {
        var isFormValid = true

        isFormValid = isFormValid and createGroupModel.name.isValid
        isFormValid = isFormValid and createGroupModel.meetingFormat.isValid
        isFormValid = isFormValid and
                createGroupModel.scheduleTime.observableField.getNotNull().isNotEmpty() and
                createGroupModel.scheduleDays.isNotEmpty()
        isFormValid = isFormValid and createGroupModel.numberOfMeetings.isValid
        isFormValid = isFormValid and createGroupModel.startDate.isValid
        //isFormValid = isFormValid and createGroupModel.tags.isValid
        isFormValid = isFormValid and createGroupModel.description.isValid
        isFormValid = isFormValid and createGroupModel.image.isValid

        return isFormValid
    }

    private fun canCreateMMGroup(): Boolean {
        var isFormValid = true

        isFormValid = isFormValid and createGroupModel.name.isValid
        isFormValid = isFormValid and
                createGroupModel.scheduleTime.observableField.getNotNull().isNotEmpty() and
                createGroupModel.scheduleDays.isNotEmpty()
        isFormValid = isFormValid and createGroupModel.numberOfMeetings.isValid
        isFormValid = isFormValid and createGroupModel.startDate.isValid
        isFormValid = isFormValid and createGroupModel.price.isValid
        isFormValid = isFormValid and createGroupModel.description.isValid
        isFormValid = isFormValid and createGroupModel.image.isValid

        return isFormValid
    }

    private fun canCreateMMIndividual(): Boolean {
        var isFormValid = true

        isFormValid = isFormValid and createGroupModel.name.isValid
        isFormValid = isFormValid and
                createGroupModel.scheduleTime.observableField.getNotNull().isNotEmpty() and
                createGroupModel.scheduleDays.isNotEmpty()
        isFormValid = isFormValid and createGroupModel.numberOfMeetings.isValid
        isFormValid = isFormValid and createGroupModel.startDate.isValid
        isFormValid = isFormValid and createGroupModel.price.isValid
        isFormValid = isFormValid and createGroupModel.description.isValid
        isFormValid = isFormValid and createGroupModel.image.isValid

        return isFormValid
    }

    private fun updateCanAddParticipant() {
        var isFormValid = true

        isFormValid = isFormValid and email.isValid

        canAddParticipant.postValue(isFormValid)
    }

    private fun checkCanAddMembers(){
        canAddMembers.postValue(createGroupModel.participants.get()?.size!! < 50)
    }

    override fun changeSchedule() {

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
        if (calendarUtil.is24TimeFormat()){
            builder.append(" ${createGroupModel.hoursOfDay}:${createGroupModel.minutes}")
        }else{
            builder.append(" ${createGroupModel.hours}:${createGroupModel.minutes} ${createGroupModel.timeType.toLowerCase()}")
        }
        createGroupModel.scheduleTime.observableField.set(builder.toString())

        createGroupModel.scheduleDays.clear()
        createGroupModel.scheduleDays.addAll(days)
    }

    override fun removeMember(member: AttendeeEntity) {
        if(selectedMembers.remove(member)){
            members.postValue(selectedMembers.toMutableList())
        }
    }

    override fun chooseMeetingCountTouch() {
        localRouter.navigateToMeetingCount()
    }

    override val members: MutableLiveData<MutableList<AttendeeEntity>> = MutableLiveData()

    override fun updateGroup(id: Long) {
        canComplete.postValue(false)

        viewModelScope.launch {
            val requestEntity =
                groupUseCase.updateGroup(id, createGroupModel.toEntity())

            canComplete.postValue(true)

            if (requestEntity.isSuccessful) {
                router.navigateToDetails(requestEntity.successModel!!)
            } else {
                if (requestEntity.errorModel!!.isNotEmpty()) {
                    showDefaultErrorMessage(requestEntity.errorModel!!.toErrorMessage())
                }
            }
        }

    }

    private fun changeStartDate() {
        val calendar = getDefaultCalendar()
        calendar.time = Date(0)
        calendar.set(Calendar.YEAR, createGroupModel.year)
        calendar.set(Calendar.MONTH, createGroupModel.month.ordinal)
        calendar.set(Calendar.DAY_OF_MONTH, createGroupModel.day)

        createGroupModel.startDate.observableField.set(START_TIME_FORMATTER.format(calendar.time))
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
        backClick()

        canOk.postValue(false)
    }

    override fun doneClick() {
        changeStartDate()
        backClick()
    }

    override fun backDateClick() {
        backClick()
        backClick()
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

    override fun getDay(): Int {
        return createGroupModel.day
    }

    override fun setYear(year: Int) {
        createGroupModel.year = year
    }

    override fun getYear(): Int {
        return createGroupModel.year
    }

    override fun onGroupSelected() {
        navigation.postValue(CreateMMGroupContract.Navigation.TO_GROUP)
    }

    override fun onIndividualSelected() {
        navigation.postValue(CreateMMGroupContract.Navigation.TO_INDIVIDUAL)
    }

    override fun onPriceClick(editor: EditText) {
        localRouter.navigateToPricePicker(editor)
    }

    override fun addMember(groupType: GroupType) {
        localRouter.navigateToAddMember(groupType)
    }

    override fun inviteToGroup(participants: List<String>) {

    }

    override fun setType(type: TimeZoneFormat.TimeType) {

    }

    override fun getType() {

    }

    override fun setMinute(minute: Short) {

    }

    override fun getMinute() {

    }

    override fun setHour(hour: Short) {

    }

    override fun getHour() {

    }

    override val group: MutableLiveData<GroupEntity> = MutableLiveData()
    override val searchVisibility = MutableLiveData<Boolean>(false)
    override val inviteVisibility = MutableLiveData<Boolean>(false)
    override val inviteButtonActive = MutableLiveData<Boolean>(false)
    override val validQuery = MutableLiveData<String>()
    override val attendees: MutableLiveData<MutableList<AttendeeEntity>> = MutableLiveData()

    override val searchResult: LiveData<PagedList<AttendeeEntity>>
        get() = searchQuery.switchMap {
            groupUseCase.searchMembers(it)
        }

    override fun loadAttendees() {
        //empty
    }

    override val selectedMembers: MutableList<AttendeeEntity> = mutableListOf()
    override val nonMembers : MutableList<String> = mutableListOf()

    override fun onQueryTextChange(query: String) {
        if (query.length > 1){
            searchQuery.postValue(query)
            validQuery.postValue(query)
        }
    }

    override fun goBack() {
        localRouter.onBack()
    }

    override fun onClearClick(member: AttendeeEntity) {
        //empty
    }

    override fun onAdd(member: AttendeeEntity) {
        selectedMembers.add(member)
        if(createGroupModel.groupType == GroupType.INDIVIDUAL){
            canAddMembers.postValue(selectedMembers.size < 1)
        }else{
            canAddMembers.postValue(selectedMembers.size < Constants.MAX_MEMBERS_COUNT)
        }
    }

    override fun onRemove(member: AttendeeEntity) {
        val itemToDelete = selectedMembers.first {
            it.email == member.email
        }
        selectedMembers.remove(itemToDelete)
        if(createGroupModel.groupType == GroupType.INDIVIDUAL){
            canAddMembers.postValue(selectedMembers.size <= 1)
        }else{
            canAddMembers.postValue(selectedMembers.size < Constants.MAX_MEMBERS_COUNT)
        }
    }

    override fun onInviteClick(emails: List<String>) {
        selectedMembers.add(
            AttendeeEntity(
            (0L - selectedMembers.size),
            "",
            emails[0],
            ""
            )
        )
        members.postValue(selectedMembers)
        localRouter.onBack()
    }

    override fun onBackClick(emails: List<String>) {
        //empty
    }

    override val meetingsCountOk: MutableLiveData<Boolean> = MutableLiveData(false)
    override fun okMeetingCountClick() {
        localRouter.onBack()
    }

    override fun setMeetingCount(count: String) {
        createGroupModel.numberOfMeetings.observableField.set(count)
    }

    override val priceOk = MutableLiveData<Boolean>(false)

    override fun okPriceClick(price: String) {
        createGroupModel.price.observableField.set(price)
        localRouter.onBack()
    }

    override fun setPrice(price: String) {
    }

    private fun String.toHours(): Int {
        return this.toInt() % 12 //% 12to avoid day increment
    }
    companion object {
        val START_TIME_FORMATTER = "dd MMMM yyyy".toDefaultFormatter()

    }
}