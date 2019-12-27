package com.doneit.ascend.presentation.main.group_info

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.doneit.ascend.domain.entity.GroupEntity
import com.doneit.ascend.domain.entity.UserEntity
import com.doneit.ascend.domain.use_case.interactor.group.GroupUseCase
import com.doneit.ascend.domain.use_case.interactor.user.UserUseCase
import com.doneit.ascend.presentation.main.base.BaseViewModelImpl
import com.doneit.ascend.presentation.utils.extensions.toErrorMessage
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class GroupInfoViewModel(
    private val router: GroupInfoContract.Router,
    private val groupUseCase: GroupUseCase,
    private val userUseCase: UserUseCase
) : BaseViewModelImpl(), GroupInfoContract.ViewModel {

    override val group = MutableLiveData<GroupEntity>()
    override val btnSubscribeVisible = MutableLiveData<Boolean>(false)
    override val btnJoinVisible = MutableLiveData<Boolean>(false)
    override val btnStartVisible = MutableLiveData<Boolean>(false)
    override val btnDeleteVisible = MutableLiveData<Boolean>(false)

    override fun setModel(model: GroupEntity) {
        group.postValue(model)

        viewModelScope.launch {
            val user = userUseCase.getUser()
            updateButtonsState(user!!, model)
        }
    }

    override fun loadData(groupId: Long) {
        showProgress(true)
        viewModelScope.launch {
            val response = groupUseCase.getGroupDetails(groupId)

            if (response.isSuccessful) {
                group.postValue(response.successModel!!)
                val user = userUseCase.getUser()
                updateButtonsState(user!!, response.successModel!!)
            }
            delay(1000)
            showProgress(false)
        }
    }

    private fun updateButtonsState(user: UserEntity, details: GroupEntity) {
        //todo refactor
        val states = mutableListOf(false, false, false, false)

        if(details.subscribed != true) {
            states[0] = true
        } else {
            states[1] = true
        }

        if(user.isMasterMind) {
            if(details.isStarting) {
                states.toFalse()
                states[2] = true
            } else if(details.participantsCount == 0) {
                states.toFalse()
                states[3] = true
            }
        }

        btnSubscribeVisible.postValue(states[0])
        btnJoinVisible.postValue(states[1])
        btnStartVisible.postValue(states[2])
        btnDeleteVisible.postValue(states[3])
    }

    private fun MutableList<Boolean>.toFalse() {
        for(i in 0 until this.size) {
            this[i] = false
        }
    }

    override fun joinToDiscussion() {
    }

    override fun subscribe() {
        //todo
    }

    override fun deleteGroup() {
        viewModelScope.launch {
            val res = groupUseCase.deleteGroup(group.value?.id ?: return@launch)

            if(res.isSuccessful) {
                router.closeActivity()
            }
        }
    }

    override fun startGroup() {
        //todo
    }

    override fun onBackPressed() {
        router.closeActivity()
    }

    override fun report(content: String) {
        viewModelScope.launch {
            group.value?.let {
                val res = userUseCase.report(content, it.owner!!.id)
                if(res.isSuccessful.not()) {
                    showDefaultErrorMessage(res.errorModel!!.toErrorMessage())
                }
            }

        }
    }
}