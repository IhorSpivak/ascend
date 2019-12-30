package com.doneit.ascend.presentation.profile.common

import android.net.Uri
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.doneit.ascend.domain.entity.ProfileEntity
import com.doneit.ascend.domain.entity.dto.GroupType
import com.doneit.ascend.domain.entity.dto.UpdateProfileModel
import com.doneit.ascend.domain.use_case.interactor.user.UserUseCase
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.base.BaseViewModelImpl
import com.doneit.ascend.presentation.models.ValidatableField
import com.doneit.ascend.presentation.models.ValidationResult
import com.doneit.ascend.presentation.models.toDTO
import com.doneit.ascend.presentation.profile.edit_bio.EditBioContract
import com.doneit.ascend.presentation.utils.extensions.toErrorMessage
import com.doneit.ascend.presentation.utils.isDescriptionValid
import com.vrgsoft.networkmanager.livedata.SingleLiveManager
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val userUseCase: UserUseCase,
    private val router: ProfileContract.Router
) : BaseViewModelImpl(),
    ProfileContract.ViewModel,
    EditBioContract.ViewModel {

    override val user = MutableLiveData<ProfileEntity>()
    override val showPhotoDialog = SingleLiveManager(Unit)
    override val showDeleteButton = MutableLiveData<Boolean>()
    override val bioValue = ValidatableField()
    override val canSaveBio = MutableLiveData<Boolean>()

    private lateinit var updateProfileModel: UpdateProfileModel

    init {
        viewModelScope.launch {
            val result = userUseCase.getProfile()

            if (result.isSuccessful) {
                showDeleteButton.postValue(result.successModel!!.image?.url.isNullOrEmpty().not())

                user.postValue(result.successModel!!)
                updateProfileModel = result.successModel!!.toDTO()
                bioValue.observableField.set(result.successModel?.bio)
            }
        }

        bioValue.validator = { s ->
            val result = ValidationResult()

            if (s.isDescriptionValid().not()) {
                result.isSussed = false
                result.errors.add(R.string.error_description)
            }

            result
        }

        bioValue.onFieldInvalidate = {
            canSaveBio.postValue(bioValue.isValid)
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
        errorMessage.call()
        router.navigateToGroupList(GroupType.DAILY, true, true)
    }

    override fun onNotificationClick() {
        router.navigateToNotifications()
    }

    override fun deleteAccount() {
        viewModelScope.launch {
            val requestEntity = userUseCase.deleteAccount()
            if (requestEntity.isSuccessful) {
                router.navigateToLogin()
            } else {
                showDefaultErrorMessage(requestEntity.errorModel!!.toErrorMessage())
            }
        }
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

    override fun updateBio(newBio: String) {
        updateProfileModel.bio = newBio
        updateProfile()
    }

    override fun navigateToEditBio() {
        router.navigateToEditBio(updateProfileModel.bio ?: "")
    }

    override fun onAvatarSelected(
        sourceUri: Uri,
        destinationUri: Uri,
        fragmentToReceiveResult: Fragment
    ) {
        router.navigateToAvatarUCropActivity(sourceUri, destinationUri, fragmentToReceiveResult)
    }

    private fun updateProfile() {
        viewModelScope.launch {
            val result = userUseCase.updateProfile(updateProfileModel)

            if (result.isSuccessful) {
                user.postValue(null)
                user.postValue(result.successModel!!)
                updateProfileModel = result.successModel!!.toDTO()
            }
        }
    }
}