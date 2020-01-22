package com.doneit.ascend.presentation.profile.common

import android.net.Uri
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import com.doneit.ascend.domain.entity.UserEntity
import com.doneit.ascend.domain.entity.dto.GroupType
import com.doneit.ascend.domain.entity.dto.UpdateProfileModel
import com.doneit.ascend.domain.use_case.interactor.question.QuestionUseCase
import com.doneit.ascend.domain.use_case.interactor.user.UserUseCase
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.base.BaseViewModelImpl
import com.doneit.ascend.presentation.models.*
import com.doneit.ascend.presentation.profile.change_location.ChangeLocationContract
import com.doneit.ascend.presentation.profile.edit_bio.EditBioContract
import com.doneit.ascend.presentation.profile.notification_settings.NotificationSettingsContract
import com.doneit.ascend.presentation.profile.notification_settings.NotificationSettingsItem
import com.doneit.ascend.presentation.profile.regular_user.age.AgeContract
import com.doneit.ascend.presentation.profile.regular_user.community.CommunityContract
import com.doneit.ascend.presentation.utils.extensions.toErrorMessage
import com.doneit.ascend.presentation.utils.getLocation
import com.doneit.ascend.presentation.utils.isDescriptionValid
import com.vrgsoft.networkmanager.livedata.SingleLiveManager
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val questionUseCase: QuestionUseCase,
    private val userUseCase: UserUseCase,
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

    override val user = MutableLiveData<UserEntity>()
    override val showPhotoDialog = SingleLiveManager(Unit)
    override val showDeleteButton = MutableLiveData<Boolean>()
    override val bioValue = ValidatableField()
    override val canSaveBio = MutableLiveData<Boolean>()
    override val questions = MutableLiveData<List<PresentationCommunityModel>>()


    private lateinit var updateProfileModel: UpdateProfileModel
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
            canSaveBio.postValue(bioValue.isValid)
        }

        userObserver = Observer {
            it?.let {
                updateProfileModel = it.toDTO()
                user.postValue(it)
            }
        }
        userLocal.observeForever(userObserver)
    }

    override fun fetchData() {
        viewModelScope.launch {
            val result = userUseCase.getProfile()

            if (result.isSuccessful) {
                showDeleteButton.postValue(result.successModel!!.image?.url.isNullOrEmpty().not())
                bioValue.observableField.set(result.successModel?.bio)
            }
        }
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
        router.navigateToGroupList(null, GroupType.MY_GROUPS, true)
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

    override fun updateProfileIcon(path: String?) {//null means remove image
        updateProfileModel.shouldUpdateIcon = true
        showDeleteButton.postValue(true)
        updateProfileModel.imagePath = path
        updateProfile()
    }

    override fun updateFullName(newFullName: String) {
        updateProfileModel.fullName = newFullName
        updateProfile()
    }

    override fun updateDisplayName(newDisplayName: String) {
        updateProfileModel.displayName = newDisplayName
        updateProfile()
    }

    override fun updateShortDescription(newShortDescription: String) {
        updateProfileModel.description = newShortDescription
        updateProfile()
    }

    override fun onEditBioClick() {
        mmRouter.navigateToEditBio()
    }

    override fun updateBio(newBio: String) {
        updateProfileModel.bio = newBio
        updateProfile()
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
        updateProfileModel.location = getLocation(city, country)
        updateProfile()
    }

    override fun onChanged(type: NotificationSettingsItem, value: Boolean) {
        when (type) {
            NotificationSettingsItem.MEETING_STARTED -> updateProfileModel.isMeetingStarted = value
            NotificationSettingsItem.NEW_GROUPS -> updateProfileModel.hasNewGroups = value
            NotificationSettingsItem.INVITE_TO_MEETING -> updateProfileModel.hasInviteToMeeting =
                value
        }
        updateProfile()
    }

    override fun goBack() {
        router.onBack()
    }

    override fun onRatingsClick() {
        router.navigateToRatings()
    }

    override fun onChangePhoneClick() {
        router.navigateToChangePhone()
    }

    override fun onLocationClick() {
        router.navigateToChangeLocation(updateProfileModel.location)
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

    override fun onCleared() {
        userLocal.removeObserver(userObserver)
        super.onCleared()
    }

    private fun updateProfile() {
        viewModelScope.launch {
            val result = userUseCase.updateProfile(updateProfileModel)

            if (result.isSuccessful.not()) {
                showDefaultErrorMessage(result.errorModel!!.toErrorMessage())
            }
        }
    }
}