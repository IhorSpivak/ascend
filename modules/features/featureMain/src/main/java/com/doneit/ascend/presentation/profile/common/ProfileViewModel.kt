package com.doneit.ascend.presentation.profile.common

import android.net.Uri
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import com.doneit.ascend.domain.entity.MonthEntity
import com.doneit.ascend.domain.entity.dto.AnswersDTO
import com.doneit.ascend.domain.entity.dto.UpdateProfileDTO
import com.doneit.ascend.domain.entity.getDefaultCalendar
import com.doneit.ascend.domain.entity.group.GroupType
import com.doneit.ascend.domain.entity.user.UserEntity
import com.doneit.ascend.domain.use_case.interactor.answer.AnswerUseCase
import com.doneit.ascend.domain.use_case.interactor.question.QuestionUseCase
import com.doneit.ascend.domain.use_case.interactor.user.UserUseCase
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.base.BaseViewModelImpl
import com.doneit.ascend.presentation.models.PresentationCommunityModel
import com.doneit.ascend.presentation.models.ValidatableField
import com.doneit.ascend.presentation.models.ValidationResult
import com.doneit.ascend.presentation.models.toPresentationCommunity
import com.doneit.ascend.presentation.models.user.PresentationUserModel
import com.doneit.ascend.presentation.models.user.toPresentation
import com.doneit.ascend.presentation.profile.change_location.ChangeLocationContract
import com.doneit.ascend.presentation.profile.edit_bio.EditBioContract
import com.doneit.ascend.presentation.profile.notification_settings.NotificationSettingsContract
import com.doneit.ascend.presentation.profile.notification_settings.NotificationSettingsItem
import com.doneit.ascend.presentation.profile.regular_user.age.AgeContract
import com.doneit.ascend.presentation.profile.regular_user.community.CommunityContract
import com.doneit.ascend.presentation.utils.extensions.toCalendar
import com.doneit.ascend.presentation.utils.extensions.toErrorMessage
import com.doneit.ascend.presentation.utils.getLocation
import com.doneit.ascend.presentation.utils.isDescriptionValid
import com.vrgsoft.networkmanager.livedata.SingleLiveManager
import kotlinx.coroutines.launch
import java.util.*


class ProfileViewModel(
    private val questionUseCase: QuestionUseCase,
    private val userUseCase: UserUseCase,
    private val answerUseCase: AnswerUseCase,
    private val router: ProfileContract.Router,
    private val mmRouter: com.doneit.ascend.presentation.profile.master_mind.MMProfileContract.Router
) : BaseViewModelImpl(),
    com.doneit.ascend.presentation.profile.master_mind.MMProfileContract.ViewModel,
    com.doneit.ascend.presentation.profile.regular_user.UserProfileContract.ViewModel,
    EditBioContract.ViewModel,
    ChangeLocationContract.ViewModel,
    NotificationSettingsContract.ViewModel,
    AgeContract.ViewModel,
    CommunityContract.ViewModel {

    override val user = MutableLiveData<PresentationUserModel>()
    override val showPhotoDialog = SingleLiveManager(Unit)
    override val bioValue = ValidatableField()
    override val birthdaySelected = MutableLiveData<Date>()
    override val questions = MutableLiveData<List<PresentationCommunityModel>>()
    override val canSave = MutableLiveData<Boolean>()

    private var selectedCommunity: String? = null
    private val minBirthday by lazy {
        val calendar = Date().toCalendar()

        val year = calendar.get(Calendar.YEAR) - DEFAULT_USER_AGE
        val month = calendar.get(Calendar.MONTH)
        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)

        calendar.time.time = 0
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, month)
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

        calendar.time
    }

    private val userLocal = userUseCase.getUserLive()
    private val userObserver: Observer<UserEntity?>

    init {

        viewModelScope.launch {
            val result = questionUseCase.getList()
            val userLocal = userUseCase.getUser()

            if (result.isSuccessful) {
                questions.postValue(result.successModel!!.community!!.answerOptions.map {
                    it.toPresentationCommunity(it == userLocal?.community)
                })
            } else {
                showDefaultErrorMessage(result.errorModel!!.toErrorMessage())
            }
        }

        bioValue.validator = { s ->
            val result = ValidationResult()

            if (s.isDescriptionValid().not()) {
                result.isSucceed = false
                result.errors.add(R.string.error_description)
            }

            result
        }

        bioValue.onFieldInvalidate = {
            canSave.postValue(bioValue.isValid)
        }

        userObserver = Observer {
            it?.let {
                user.postValue(it.toPresentation())
                val birthday = it.birthday ?: minBirthday
                birthdaySelected.postValue(birthday)
                bioValue.observableField.set(it.bio ?: "")
            }
        }
        userLocal.observeForever(userObserver)
    }

    override fun fetchData() {
        viewModelScope.launch {
            userUseCase.updateCurrentUserData()
        }
    }

    override fun hasIcon(): Boolean {
        return user.value?.image?.url.isNullOrEmpty().not()
    }

    override fun onEditPhotoClick() {
        showPhotoDialog.call()
    }

    override fun onLogoutClick() {
        viewModelScope.launch {
            val requestEntity = userUseCase.signOut()
            if (requestEntity.isSuccessful) {
                router.navigateToLogin()
            } else {
                showDefaultErrorMessage(requestEntity.errorModel!!.toErrorMessage())
            }
        }
    }

    override fun onTermsClick() {
        router.navigateToTerms()
    }

    override fun onPolicyClick() {
        router.navigateToPrivacyPolicy()
    }

    override fun onSeeMyGroupsClick() {
        router.navigateToGroupList(user.value?.id, GroupType.MY_GROUPS, true, null)
    }

    override fun onNotificationClick() {
        router.navigateToNotifications()
    }

    override fun onAgeClick() {
        router.navigateToSetAge()
    }

    override fun onCommunityClick() {
        router.navigateToSetCommunity()
    }

    override fun updateProfileIcon(path: String) {
        updateProfile(UpdateProfileDTO(imagePath = path))
    }

    override fun removeProfileIcon() {
        updateProfile(UpdateProfileDTO(removeImage = true))
    }

    override fun updateFullName(newFullName: String) {
        updateProfile(UpdateProfileDTO(fullName = newFullName))
    }

    override fun updateDisplayName(newDisplayName: String) {
        updateProfile(UpdateProfileDTO(displayName = newDisplayName))
    }

    override fun updateShortDescription(newShortDescription: String) {
        updateProfile(UpdateProfileDTO(description = newShortDescription))
    }

    override fun onEditBioClick() {
        mmRouter.navigateToEditBio()
    }

    override fun updateBio(newBio: String) {
        updateProfile(UpdateProfileDTO(bio = newBio))
    }

    override fun onAvatarSelected(
        sourceUri: Uri,
        destinationUri: Uri,
        fragmentToReceiveResult: Fragment
    ) {
        router.navigateToAvatarUCropActivity(sourceUri, destinationUri, fragmentToReceiveResult)
    }

    override fun onMMFollowedClick() {
        router.navigateToMMFollowed()
    }

    override fun updateLocation(city: String, country: String) {
        val location = getLocation(city, country)
        updateProfile(UpdateProfileDTO(location = location))
    }

    override fun onBirthdaySelected(year: Int, month: MonthEntity, day: Int) {
        val calendar = getDefaultCalendar()
        calendar.time.time = 0

        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, month.ordinal)
        calendar.set(Calendar.DAY_OF_MONTH, day)

        if (calendar.time.after(minBirthday)) {
            birthdaySelected.postValue(minBirthday)
        } else {
            birthdaySelected.postValue(calendar.time)
        }
    }

    override fun saveBirthday() {
        birthdaySelected.value?.let {
            updateProfile(UpdateProfileDTO(birthday = it))
            router.onBack()
        }
    }

    override fun onChanged(type: NotificationSettingsItem, value: Boolean) {
        val model = when (type) {
            NotificationSettingsItem.MEETING_STARTED -> UpdateProfileDTO(isMeetingStarted = true)
            NotificationSettingsItem.NEW_GROUPS -> UpdateProfileDTO(hasNewGroups = true)
            NotificationSettingsItem.INVITE_TO_MEETING -> UpdateProfileDTO(hasInviteToMeeting = true)
        }
        updateProfile(model)
    }

    override fun setSelectedCommunity(community: String) {
        selectedCommunity = community
        canSave.postValue(true)
    }

    override fun saveCommunity() {
        selectedCommunity?.let { newCommunity ->
            if (newCommunity == userLocal.value?.community) {
                router.onBack()
            } else {
                canSave.postValue(false)
                viewModelScope.launch {
                    val result = answerUseCase.createAnswers(AnswersDTO(newCommunity, listOf()))

                    if (result.isSuccessful) {
                        router.onBack()
                    } else {
                        showDefaultErrorMessage(result.errorModel!!.toErrorMessage())
                    }
                    canSave.postValue(true)
                }
            }
        }
    }

    override fun goBack() {
        router.onBack()
    }

    override fun onRatingsClick() {
        router.navigateToRatings()
    }

    override fun onMMCommunityClick() {
        //todo:
        router.navigateToSetCommunity()
    }

    override fun onChangePhoneClick() {
        router.navigateToChangePhone()
    }

    override fun onLocationClick() {
        router.navigateToChangeLocation(userLocal.value?.location)
    }

    override fun onChangePasswordClick() {
        router.navigateToChangePassword()
    }

    override fun onEditEmailClick() {
        router.navigateToEditEmail()
    }

    override fun onNotificationSettingsClick() {
        router.navigateToNotificationSettings()
    }

    override fun onPaymentsClick(isMasterMind: Boolean) {
        router.navigateToPayments(isMasterMind)
    }

    override fun onBlockedUsersClick() {
        router.navigateToBlockedUsers()
    }

    override fun onCleared() {
        userLocal.removeObserver(userObserver)
        super.onCleared()
    }

    private fun updateProfile(dto: UpdateProfileDTO) {
        viewModelScope.launch {
            val result = userUseCase.updateProfile(dto)

            if (result.isSuccessful.not()) {
                showDefaultErrorMessage(result.errorModel!!.toErrorMessage())
            }
        }
    }

    companion object {
        private const val DEFAULT_USER_AGE = 18
    }
}