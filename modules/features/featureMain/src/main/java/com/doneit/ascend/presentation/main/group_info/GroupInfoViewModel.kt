package com.doneit.ascend.presentation.main.group_info

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.doneit.ascend.domain.entity.AttendeeEntity
import com.doneit.ascend.domain.entity.dto.CancelGroupDTO
import com.doneit.ascend.domain.entity.dto.PaymentType
import com.doneit.ascend.domain.entity.dto.SubscribeGroupDTO
import com.doneit.ascend.domain.entity.group.GroupEntity
import com.doneit.ascend.domain.entity.group.GroupStatus
import com.doneit.ascend.domain.entity.group.GroupType
import com.doneit.ascend.domain.entity.user.UserEntity
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
    override val isEditable = MutableLiveData<Boolean>(false)
    override val isMM = MutableLiveData<Boolean>(false)
    override val isOwner = MutableLiveData<Boolean>(false)
    override val isSubscribed = MutableLiveData<Boolean>(false)
    override val starting = MutableLiveData<Boolean>(false)

    override val isBlocked: Boolean
        get() {
            val isInitialized = group.value != null
            return isInitialized && group.value?.blocked == true
        }
    override val isSupport = MutableLiveData<Boolean>(false)

    override fun loadData(groupId: Long) {
        showProgress(true)
        viewModelScope.launch {
            val response = groupUseCase.getGroupDetails(groupId)

            if (response.isSuccessful) {
                group.postValue(response.successModel!!)
                isSupport.postValue(response.successModel?.groupType != GroupType.SUPPORT)
                val user = userUseCase.getUser()
                isMM.postValue(user!!.isMasterMind)
                isOwner.postValue(user.id == response.successModel!!.owner?.id)
                updateButtonsState(user!!, response.successModel!!)
            }
            showProgress(false)
        }
    }

    private fun updateButtonsState(user: UserEntity, details: GroupEntity) {
        details.apply {
            btnJoinedVisible.postValue(subscribed)
            btnJoinVisible.postValue(inProgress && subscribed!!)
            isEditable.postValue(status != GroupStatus.ENDED)
            starting.postValue(status == GroupStatus.ACTIVE)
            btnStartVisible.postValue(status != GroupStatus.STARTED)
            btnDeleteVisible.postValue(participantsCount == 0)
            btnSubscribeVisible.postValue(subscribed != true && user.id != details.owner?.id)
            if (user.id == details.owner?.id){
                btnJoinVisible.postValue(inProgress && status == GroupStatus.STARTED)
            }
            isSubscribed.postValue(subscribed)
        }
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

    override fun leaveGroup() {
        viewModelScope.launch {
            val res = groupUseCase.leaveGroup(group.value?.id ?: return@launch)

            if (res.isSuccessful) {
                router.onBack()
            }
        }
    }

    override fun cancelGroup(reason: String) {
        group.value?.let {
            viewModelScope.launch {
                val response = groupUseCase.cancelGroup(CancelGroupDTO(it.id, reason))

                if (response.isSuccessful) {
                } else {
                    showDefaultErrorMessage(response.errorModel!!.toErrorMessage())
                }
            }
        }
    }

    override fun inviteToGroup(reason: List<String>) {

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

    override fun onViewClick(attendees: List<AttendeeEntity>) {
        router.navigateToViewAttendees(attendees, group.value!!)
    }

    override fun onDuplicateClick(group: GroupEntity) {
        router.navigateToDuplicateGroup(group)
    }

    override fun onEditClick(group: GroupEntity) {
        router.navigateToEditGroup(group)
    }

    override fun onCancelClick(group: GroupEntity) {
    }

    override fun removeMember(attendee: AttendeeEntity) {

    }
}