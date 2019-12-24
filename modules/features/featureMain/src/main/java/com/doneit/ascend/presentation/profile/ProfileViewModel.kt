package com.doneit.ascend.presentation.profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.doneit.ascend.domain.entity.ProfileEntity
import com.doneit.ascend.domain.entity.dto.GroupType
import com.doneit.ascend.domain.entity.dto.UpdateProfileModel
import com.doneit.ascend.domain.use_case.interactor.user.UserUseCase
import com.doneit.ascend.presentation.main.base.BaseViewModelImpl
import com.doneit.ascend.presentation.models.toDTO
import com.doneit.ascend.presentation.utils.extensions.toErrorMessage
import com.vrgsoft.annotations.CreateFactory
import com.vrgsoft.annotations.ViewModelDiModule
import com.vrgsoft.networkmanager.livedata.SingleLiveManager
import kotlinx.coroutines.launch

@CreateFactory
@ViewModelDiModule
class ProfileViewModel(
    private val userUseCase: UserUseCase,
    private val router: ProfileContract.Router
) : BaseViewModelImpl(), ProfileContract.ViewModel {

    override val user = MutableLiveData<ProfileEntity>()
    override val showPhotoDialog = SingleLiveManager(Unit)

    private lateinit var updateProfileModel: UpdateProfileModel

    init {
        viewModelScope.launch {
            val result = userUseCase.getProfile()

            if (result.isSuccessful) {
                user.postValue(result.successModel!!)
                updateProfileModel = result.successModel!!.toDTO()
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
        errorMessage.call()
        router.navigateToGroupList(GroupType.DAILY, true, true)
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
        updateProfileModel.imagePath = path
        updateProfile()
    }

    private fun updateProfile() {
        viewModelScope.launch {
            val result = userUseCase.updateProfile(updateProfileModel)

            if(result.isSuccessful) {
                user.postValue(result.successModel!!)
                updateProfileModel = result.successModel!!.toDTO()
            }
        }
    }
}