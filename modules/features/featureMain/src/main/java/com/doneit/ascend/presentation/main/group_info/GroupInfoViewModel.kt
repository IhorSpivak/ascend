package com.doneit.ascend.presentation.main.group_info

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.doneit.ascend.domain.entity.UserEntity
import com.doneit.ascend.domain.entity.dto.PaymentType
import com.doneit.ascend.domain.entity.dto.SubscribeGroupDTO
import com.doneit.ascend.domain.entity.group.GroupEntity
import com.doneit.ascend.domain.use_case.interactor.cards.CardsUseCase
import com.doneit.ascend.domain.use_case.interactor.group.GroupUseCase
import com.doneit.ascend.domain.use_case.interactor.master_mind.MasterMindUseCase
import com.doneit.ascend.domain.use_case.interactor.user.UserUseCase
import com.doneit.ascend.presentation.main.base.BaseViewModelImpl
import com.doneit.ascend.presentation.models.PresentationCardModel
import com.doneit.ascend.presentation.models.toPresentation
import com.doneit.ascend.presentation.utils.ButtonType
import com.doneit.ascend.presentation.utils.extensions.toErrorMessage
import com.doneit.ascend.presentation.utils.getButtonType
import com.vrgsoft.annotations.CreateFactory
import com.vrgsoft.annotations.ViewModelDiModule
import kotlinx.coroutines.launch

@CreateFactory
@ViewModelDiModule
class GroupInfoViewModel(
    private val router: GroupInfoContract.Router,
    private val groupUseCase: GroupUseCase,
    private val userUseCase: UserUseCase,
    private val cardsUseCase: CardsUseCase,
    private val mmUseCase: MasterMindUseCase
) : BaseViewModelImpl(), GroupInfoContract.ViewModel {

    override val group = MutableLiveData<GroupEntity>()
    override val cards = cardsUseCase.getAllCards().map { list -> list.map { it.toPresentation() } }
    override val btnSubscribeVisible = MutableLiveData<Boolean>(false)
    override val btnJoinVisible = MutableLiveData<Boolean>(false)
    override val btnStartVisible = MutableLiveData<Boolean>(false)
    override val btnDeleteVisible = MutableLiveData<Boolean>(false)
    override val btnJoinedVisible = MutableLiveData<Boolean>(false)

    override val isBlocked: Boolean
        get() {
            val isInitialized = group.value != null
            return isInitialized && group.value?.blocked == true
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
            showProgress(false)
        }
    }

    private fun updateButtonsState(user: UserEntity, details: GroupEntity) {
        //todo refactor
        val states = mutableListOf(false, false, false, false, false)

        when (getButtonType(user, details)) {
            ButtonType.SUBSCRIBE -> states[0] = true
            ButtonType.JOIN_TO_DISCUSSION -> states[1] = true
            ButtonType.START_GROUP -> states[2] = true
            ButtonType.DELETE_GROUP -> states[3] = true
            ButtonType.SUBSCRIBED -> states[4] = true
        }

        btnSubscribeVisible.postValue(states[0])
        btnJoinVisible.postValue(states[1])
        btnStartVisible.postValue(states[2])
        btnDeleteVisible.postValue(states[3])
        btnJoinedVisible.postValue(states[4])
    }

    override fun joinToDiscussion() {
        router.navigateToVideoChat(group.value!!.id)
    }

    override fun subscribe(card: PresentationCardModel) {
        viewModelScope.launch {
            val requestModel = SubscribeGroupDTO(
                group.value!!.id,
                card.id,
                PaymentType.CARD
            )
            val result = groupUseCase.subscribe(requestModel)

            if (result.isSuccessful) {
                loadData(group.value!!.id)
            } else {
                showDefaultErrorMessage(result.errorModel!!.toErrorMessage())
            }
        }
    }

    override fun deleteGroup() {
        viewModelScope.launch {
            val res = groupUseCase.deleteGroup(group.value?.id ?: return@launch)

            if (res.isSuccessful) {
                router.onBack()
            }
        }
    }

    override fun startGroup() {
        router.navigateToVideoChat(group.value!!.id)
    }

    override fun onAddPaymentClick() {
        router.navigateToAddPaymentMethod()
    }

    override fun onBackPressed() {
        router.onBack()
    }

    override fun report(content: String) {
        viewModelScope.launch {
            group.value?.let {
                val res = userUseCase.report(content, it.owner!!.id.toString())
                if (res.isSuccessful.not()) {
                    showDefaultErrorMessage(res.errorModel!!.toErrorMessage())
                }
            }

        }
    }

    override fun onMMClick() {
        group.value?.owner?.id?.let {
            router.navigateToMMInfo(it)
        }
    }
}