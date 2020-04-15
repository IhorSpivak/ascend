package com.doneit.ascend.presentation.main.create_group

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
import com.doneit.ascend.presentation.models.group.toUpdateEntity
import com.doneit.ascend.presentation.models.group.toUpdateWebinarEntity
import com.doneit.ascend.presentation.models.group.toWebinarEntity
import com.doneit.ascend.presentation.utils.*
import com.doneit.ascend.presentation.utils.extensions.toDefaultFormatter
import com.doneit.ascend.presentation.utils.extensions.toErrorMessage
import com.google.android.material.textfield.TextInputEditText
import com.vrgsoft.networkmanager.livedata.SingleLiveManager
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
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
    private val timeChooserState = MutableLiveData<Boolean>(false)

    init {
        createGroupModel.name.validator = { s ->
            val result = ValidationResult()

            if (s.isValidGroupName().not()) {
                result.isSucceed = false
                when (createGroupModel.groupType) {
                    GroupType.WEBINAR -> result.errors.add(R.string.error_webinar_name)
                    else -> result.errors.add(R.string.error_group_name)
                }
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
            val groupTypeRequest = if (createGroupModel.groupType == GroupType.WEBINAR) {
                createGroupModel.toWebinarEntity(calendarUtil.is24TimeFormat())
            } else {
                createGroupModel.toEntity()
            }
            val requestEntity =
                groupUseCase.createGroup(groupTypeRequest)

            canComplete.postValue(true)

            if (requestEntity.isSuccessful) {
                router.navigateToDetailsNoBackStack(requestEntity.successModel!!)
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
            GroupType.WEBINAR -> localRouter.navigateToCreateWebinar(args, group, what)
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

    override fun chooseScheduleTouch(position: Int) {
        updateCanOk()
        localRouter.navigateToWebinarCalendarPiker(position)
    }

    override fun onSelectStartDate() {
        updateCanOk()
        localRouter.navigateToWebinarDatePiker()
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
    override fun okWebinarTimeClick(selectedDay: Int, date: Calendar, position: Int) {
        if (position == 0) {
            createGroupModel.scheduleDays.apply {
                if (size == 0) {
                    add(CalendarDayEntity.values()[selectedDay - 1])
                } else {
                    set(0, CalendarDayEntity.values()[selectedDay - 1])
                }
            }
            createGroupModel.webinarSchedule[position].observableField.apply {
                date.set(Calendar.DAY_OF_WEEK, selectedDay)
                if (calendarUtil.is24TimeFormat()) {
                    set(TIME_24_FORMAT.format(date.time))
                } else {
                    set(TIME_12_FORMAT.format(date.time))
                }
            }
            createGroupModel.timeList[position].time = date.time
            createGroupModel.actualStartTime.apply {
                set(Calendar.DAY_OF_WEEK, selectedDay)
                set(Calendar.HOUR_OF_DAY, date.get(Calendar.HOUR_OF_DAY))
                set(Calendar.MINUTE, date.get(Calendar.MINUTE))
            }
        } else {
            createGroupModel.scheduleDays.apply {
                if (size < position + 1) {
                    add(CalendarDayEntity.values()[selectedDay - 1])
                } else {
                    set(position, CalendarDayEntity.values()[selectedDay - 1])
                }
            }
            createGroupModel.webinarSchedule[position].observableField.apply {
                date.set(Calendar.DAY_OF_WEEK, selectedDay)
                createGroupModel.timeList[position].time = date.time
                if (calendarUtil.is24TimeFormat()) {
                    set(TIME_24_FORMAT.format(date.time))
                } else {
                    set(TIME_12_FORMAT.format(date.time))
                }
            }
        }
        localRouter.onBack()
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

    override fun updateTimeChooserOk(state: Boolean) {
        timeChooserState.postValue(state)
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
            createGroupModel.isPrivate.getNotNull() && createGroupModel.groupType == GroupType.MASTER_MIND -> canCreateMMGroup()
            createGroupModel.isPrivate.getNotNull().not() && createGroupModel.groupType == GroupType.MASTER_MIND -> canCreateMMIndividual()
            createGroupModel.groupType == GroupType.WEBINAR -> canCreateWebinar()
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
    private fun canCreateWebinar(): Boolean {
        var isFormValid = true

        isFormValid = isFormValid and createGroupModel.name.isValid
        isFormValid = isFormValid and
                createGroupModel.webinarSchedule[0].observableField.getNotNull().isNotEmpty()
        isFormValid = isFormValid and createGroupModel.numberOfMeetings.isValid
        isFormValid = isFormValid and createGroupModel.startDate.isValid
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
        deletedMembers.add(member)
        membersToDelete.postValue(deletedMembers.toMutableList())
    }

    override fun chooseMeetingCountTouch(group: GroupEntity?, what: GroupAction?) {
        localRouter.navigateToMeetingCount(group, what)
    }

    override fun updateListOfTimes(position: Int, remove: Boolean) {
        if (remove) {
            if (position == 0){
                createGroupModel.webinarSchedule.dropLast(1)
                createGroupModel.timeList.dropLast(1)
                createGroupModel.scheduleDays.dropLast(1)
            }else {
                createGroupModel.webinarSchedule.removeAt(position)
                createGroupModel.timeList.removeAt(position)
                createGroupModel.scheduleDays.apply {
                    if (size >= position + 1) {
                        removeAt(position)
                    }
                }
            }
        } else {
            createGroupModel.webinarSchedule.add(ValidatableField())
            createGroupModel.timeList.add(getDefaultCalendar())
        }
        newScheduleItem.postValue(createGroupModel.webinarSchedule)
    }

    override fun updateListOfTimes(remove: Boolean) {
        if (remove) {
            createGroupModel.webinarSchedule.dropLast(1)
            createGroupModel.timeList.dropLast(1)
        } else {
            createGroupModel.webinarSchedule.add(ValidatableField())
            createGroupModel.timeList.add(getDefaultCalendar())
        }
        newScheduleItem.postValue(createGroupModel.webinarSchedule)
    }

    override fun updateFields(group: GroupEntity, what: String) {
        createGroupModel.apply {
            when(what){
                GroupAction.DUPLICATE.toString() ->{name.observableField.set(group.name.plus("(2)"))}
                GroupAction.EDIT.toString() ->{name.observableField.set(group.name)}
            }
            isPrivate.set(group.isPrivate)
            numberOfMeetings.observableField.set(group.meetingsCount.toString())
            description.observableField.set(group.description)
            actualStartTime.time = group.startTime
            groupType = GroupType.values()[group.groupType!!.ordinal]
            startDate.observableField.set(SimpleDateFormat("dd MMMM yyyy").format(actualStartTime.time))
            scheduleDays.addAll(group.daysOfWeek)
            scheduleDays.forEachIndexed { index, day ->
                if (index != 0) {
                    webinarSchedule.add(ValidatableField())
                    timeList.add(getDefaultCalendar())
                }
                timeList[index].apply {
                    if (calendarUtil.is24TimeFormat()){
                        group.dates?.get(index).let {
                            time = HOUR_24_ONLY_FORMAT.parse(it)
                        }
                        set(Calendar.DAY_OF_WEEK, day.ordinal + 1)
                        webinarSchedule[index].observableField.set(TIME_24_FORMAT.format(timeList[index].time))
                    }else{
                        group.dates?.get(index).let {
                            time = HOUR_24_ONLY_FORMAT.parse(it)
                        }
                        set(Calendar.DAY_OF_WEEK, day.ordinal + 1)
                        webinarSchedule[index].observableField.set(TIME_12_FORMAT.format(timeList[index].time))
                    }
                }
            }
            newScheduleItem.postValue(webinarSchedule)
            group.themes?.let {
                themesOfMeeting = it.map {theme ->
                    ValidatableField().apply {
                        observableField.set(theme)
                    }
                }.toMutableList()
            }
            themes.postValue(themesOfMeeting)
        }
        members.postValue(group.attendees?.toMutableList())
        selectedMembers.addAll(group.attendees?: emptyList())
    }

    override val canTimeChooserOk: LiveData<Boolean> = timeChooserState.switchMap { liveData { emit(it) } }

    override val members: MutableLiveData<MutableList<AttendeeEntity>> = MutableLiveData()
    override val membersToDelete: MutableLiveData<MutableList<AttendeeEntity>>  = MutableLiveData()
    override val deletedMembers: MutableList<AttendeeEntity> = mutableListOf()

    override fun updateGroup(group: GroupEntity) {
        canComplete.postValue(false)

        viewModelScope.launch {
            group.let {group ->
                val groupTypeRequest = if (createGroupModel.groupType == GroupType.WEBINAR) {
                    createGroupModel.toUpdateWebinarEntity(group)
                } else {
                    createGroupModel.toUpdateEntity(group.attendees?.map { it.email?:"" }?: emptyList())
                }
                groupUseCase.updateGroup(group.id, groupTypeRequest).let { response ->
                    canComplete.postValue(true)
                    if (response.isSuccessful) {
                        router.navigateToDetailsNoBackStack(response.successModel!!)
                    } else {
                        if (response.errorModel!!.isNotEmpty()) {
                            showDefaultErrorMessage(response.errorModel!!.toErrorMessage())
                        }
                    }
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
        createGroupModel.apply {
            selectedDays.clear()
            startDate.observableField.set(START_TIME_FORMATTER.format(calendar.time))
        }
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

    override val themesOfMeeting: MutableLiveData<Int> = MutableLiveData()
    override val newScheduleItem: MutableLiveData<MutableList<ValidatableField>> = MutableLiveData(mutableListOf(
        ValidatableField()
    ))
    override val themes: MutableLiveData<MutableList<ValidatableField>> = MutableLiveData()

    override fun onPriceClick(editor: TextInputEditText) {
        localRouter.navigateToPricePicker(editor)
    }

    override fun addMember(groupType: GroupType) {
        localRouter.navigateToAddMember(groupType)
    }

    override fun inviteToGroup(participants: List<String>) {

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
        when(createGroupModel.groupType){
            GroupType.INDIVIDUAL -> canAddMembers.postValue((attendees.value?.size?: 0) < 1)
            GroupType.MASTER_MIND -> canAddMembers.postValue(selectedMembers.size + (attendees.value?.size?: 0) < 50)
            GroupType.WEBINAR -> canAddMembers.postValue(selectedMembers.size + (attendees.value?.size?: 0) < 3)
            GroupType.SUPPORT -> canAddMembers.postValue(selectedMembers.size + (attendees.value?.size?: 0) < 50)
        }
    }

    override fun onRemove(member: AttendeeEntity) {
        val itemToDelete = selectedMembers.first {
            it.email == member.email
        }
        selectedMembers.remove(itemToDelete)
        when(createGroupModel.groupType){
            GroupType.INDIVIDUAL -> canAddMembers.postValue((attendees.value?.size?: 0) < 1)
            GroupType.MASTER_MIND -> canAddMembers.postValue(selectedMembers.size + (attendees.value?.size?: 0) < 50)
            GroupType.WEBINAR -> canAddMembers.postValue(selectedMembers.size + (attendees.value?.size?: 0) < 3)
            GroupType.SUPPORT -> canAddMembers.postValue(selectedMembers.size + (attendees.value?.size?: 0) < 50)
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

    override fun updateNumberOfMeeting(count: Int) {
        themesOfMeeting.postValue(count)
        createGroupModel.numberOfMeetings.observableField.set(count.toString())
        val size = createGroupModel.themesOfMeeting.size
        when{
            size == 0 -> {
                for (i in 1..count) {
                    createGroupModel.themesOfMeeting.add(ValidatableField())
                }
            }
            size > count ->{
                for (i in count  until size ) {
                    createGroupModel.themesOfMeeting.removeAt(createGroupModel.themesOfMeeting.size - 1)
                }
            }
            size < count ->{
                val range = count - size
                for (i in 1..range) {
                    createGroupModel.themesOfMeeting.add(ValidatableField())
                }
            }
        }
        themes.postValue(createGroupModel.themesOfMeeting)
    }

    override val priceOk = MutableLiveData<Boolean>(false)

    override fun okPriceClick(price: String) {
        createGroupModel.price.observableField.set(price)
        localRouter.onBack()
    }

    override fun setPrice(price: String) {
    }

    override fun cancelDateSelection() {
        localRouter.onBack()
    }

    override fun okDateSelection(date: Calendar) {
        createGroupModel.actualStartTime.apply {
            set(date.get(Calendar.YEAR), date.get(Calendar.MONTH), date.get(Calendar.DAY_OF_MONTH))
        }
        createGroupModel.startDate.observableField.set("dd MMMM yyyy".toDefaultFormatter().format(date.time))
        localRouter.onBack()
    }

    private fun String.toHours(): Int {
        return this.toInt() % 12 //% 12to avoid day increment
    }
    companion object {
        val START_TIME_FORMATTER = "dd MMMM yyyy".toDefaultFormatter()
        val TIME_24_FORMAT = "EEE, HH:mm".toDefaultFormatter()
        val TIME_12_FORMAT = "EEE, hh:mm a".toDefaultFormatter()
        val WEEK_ONLY_FORMAT = "EEE".toDefaultFormatter()
        val HOUR_12_ONLY_FORMAT = "hh:mm a".toDefaultFormatter()
        val HOUR_24_ONLY_FORMAT = "HH:mm".toDefaultFormatter().apply{ timeZone = TimeZone.getTimeZone("GMT") }
    }
}