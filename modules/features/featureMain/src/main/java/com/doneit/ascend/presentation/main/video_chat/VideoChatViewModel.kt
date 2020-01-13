package com.doneit.ascend.presentation.main.video_chat

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.doneit.ascend.domain.use_case.interactor.group.GroupUseCase
import com.doneit.ascend.domain.use_case.interactor.user.UserUseCase
import com.doneit.ascend.presentation.main.base.BaseViewModelImpl
import com.doneit.ascend.presentation.models.StartVideoModel
import com.vrgsoft.annotations.CreateFactory
import com.vrgsoft.annotations.ViewModelDiModule
import kotlinx.coroutines.launch


@CreateFactory
@ViewModelDiModule
class VideoChatViewModel(
    private val router: VideoChatContract.Router,
    private val userUseCase: UserUseCase,
    private val groupUseCase: GroupUseCase
) : BaseViewModelImpl(), VideoChatContract.ViewModel {

    override val credentials = MutableLiveData<StartVideoModel>()

    override fun init(groupId: Long) {
        viewModelScope.launch {
            val user = userUseCase.getUser()
            val creds = groupUseCase.getCredentials(groupId)

            credentials.postValue(
                StartVideoModel(
                    user!!.isMasterMind,
                    creds.successModel!!.name,
                    creds.successModel!!.token
                )
            )
        }
    }

    override fun onBackClick() {
        router.onBack()
    }
}