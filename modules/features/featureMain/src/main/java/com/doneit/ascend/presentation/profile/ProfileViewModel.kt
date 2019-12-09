package com.doneit.ascend.presentation.profile

import androidx.lifecycle.viewModelScope
import com.doneit.ascend.domain.use_case.interactor.user.UserUseCase
import com.doneit.ascend.presentation.main.base.BaseViewModelImpl
import com.doneit.ascend.presentation.utils.extensions.toErrorMessage
import com.vrgsoft.annotations.CreateFactory
import com.vrgsoft.annotations.ViewModelDiModule
import kotlinx.coroutines.launch

@CreateFactory
@ViewModelDiModule
class ProfileViewModel(
    private val userUseCase: UserUseCase,
    private val router: ProfileContract.Router
) : BaseViewModelImpl(), ProfileContract.ViewModel {

    override fun signOut() {
        viewModelScope.launch {
            val requestEntity = userUseCase.signOut()
            if (requestEntity.isSuccessful) {
                router.navigateToLogin()
            } else {
                showDefaultErrorMessage(requestEntity.errorModel!!.toErrorMessage())
            }
        }
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
}