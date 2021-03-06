package com.doneit.ascend.presentation.main.group_info

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.doneit.ascend.domain.entity.AttendeeEntity
import com.doneit.ascend.domain.entity.ParticipantEntity
import com.doneit.ascend.domain.entity.dto.CancelGroupDTO
import com.doneit.ascend.domain.entity.dto.PaymentType
import com.doneit.ascend.domain.entity.dto.SubscribeGroupDTO
import com.doneit.ascend.domain.entity.group.GroupEntity
import com.doneit.ascend.domain.entity.group.GroupStatus
import com.doneit.ascend.domain.entity.group.GroupType
import com.doneit.ascend.domain.entity.user.UserEntity
import com.doneit.ascend.domain.use_case.interactor.cards.CardsUseCase
import com.doneit.ascend.domain.use_case.interactor.chats.ChatUseCase
import com.doneit.ascend.domain.use_case.interactor.group.GroupUseCase
import com.doneit.ascend.domain.use_case.interactor.user.UserUseCase
import com.doneit.ascend.presentation.main.base.BaseViewModelImpl
import com.doneit.ascend.presentation.main.home.community_feed.share_post.SharePostBottomSheetFragment
import com.doneit.ascend.presentation.models.PresentationCardModel
import com.doneit.ascend.presentation.models.group.toUpdatePrivacyGroupDTO
import com.doneit.ascend.presentation.models.toPresentation
import com.doneit.ascend.presentation.utils.convertCommunityToResId
import com.doneit.ascend.presentation.utils.extensions.toErrorMessage
import com.vrgsoft.annotations.CreateFactory
import com.vrgsoft.annotations.ViewModelDiModule
import com.vrgsoft.networkmanager.livedata.SingleLiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@CreateFactory
@ViewModelDiModule
class GroupInfoViewModel(
    private val router: GroupInfoContract.Router,
    private val groupUseCase: GroupUseCase,
    private val userUseCase: UserUseCase,
    private val chatUseCase: ChatUseCase,
    cardsUseCase: CardsUseCase
) : BaseViewModelImpl(), GroupInfoContract.ViewModel {

    override val group = MutableLiveData<GroupEntity>()
    override val cards = cardsUseCase.getAllCards().map { list -> list.map { it.toPresentation() } }
    override val btnSubscribeVisible = MutableLiveData(false)
    override val btnJoinVisible = MutableLiveData(false)
    override val btnStartVisible = MutableLiveData(false)
    override val btnDeleteVisible = MutableLiveData(false)
    override val btnJoinedVisible = MutableLiveData(false)
    override val isEditable = MutableLiveData(false)
    override val isMM = MutableLiveData(false)
    override val isOwner = MutableLiveData(false)
    override val isSubscribed = MutableLiveData(false)
    override val starting = MutableLiveData(false)
    override val users: MutableLiveData<List<ParticipantEntity>> = MutableLiveData()
    override val supportTitle = MutableLiveData<Int>()
    override val closeDialog = SingleLiveEvent<Boolean>()
    override val isBlocked: Boolean
        get() {
            val isInitialized = group.value != null
            return isInitialized && group.value?.blocked == true
        }
    override val isSupport = MutableLiveData(false)

    private val user = MutableLiveData<UserEntity>()

    override fun loadData(groupId: Long) {
        showProgress(true)
        viewModelScope.launch {
            val response = groupUseCase.getGroupDetails(groupId)
            if (response.isSuccessful) {
                user.value = userUseCase.getUser()
                val user = requireNotNull(user.value)
                isOwner.value = user.id == response.successModel!!.owner?.id
                group.value = requireNotNull(response.successModel)
                isSupport.value = response.successModel?.groupType != GroupType.SUPPORT
                supportTitle.value = convertCommunityToResId(
                    user.community.orEmpty(),
                    group.value?.groupType
                )
                isMM.value = user.isMasterMind
                if (response.successModel!!.participantsCount!! > 0) {
                    groupUseCase.getParticipantList(groupId, null, null).let {
                        if (it.isSuccessful) {
                            users.value = it.successModel
                        }
                    }
                }
                updateButtonsState(user, response.successModel!!)
            } else {
                showDefaultErrorMessage(response.errorModel!!.toErrorMessage())
            }
            showProgress(false)
        }
    }

    private fun updateButtonsState(user: UserEntity, details: GroupEntity) {
        viewModelScope.launch(Dispatchers.Main) {
            details.apply {
                btnJoinedVisible.value = subscribed
                btnJoinVisible.value = (inProgress || isStarting) && subscribed!! && status != GroupStatus.CANCELLED && status != GroupStatus.ENDED
                isEditable.value = status != GroupStatus.ENDED
                starting.value = status == GroupStatus.ACTIVE
                btnStartVisible.value = status != GroupStatus.STARTED
                btnDeleteVisible.value = participantsCount == 0
                btnSubscribeVisible.value = subscribed != true && user.id != details.owner?.id && price == 0f
                if (user.id == details.owner?.id) {
                    btnJoinVisible.value = inProgress && status == GroupStatus.STARTED
                }
                isSubscribed.value = subscribed
            }
        }
    }

    override fun joinToDiscussion() {
        router.navigateToVideoChat(group.value!!.id, group.value!!.groupType!!)
    }

    override fun subscribe(card: PresentationCardModel) {
        showProgress(true)
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
            showProgress(false)
        }
    }

    override fun subscribe() {
        viewModelScope.launch {
            val requestModel = SubscribeGroupDTO(
                group.value!!.id,
                null,
                null
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
            } else {
                showDefaultErrorMessage(res.errorModel!!.toErrorMessage())
            }
        }
    }

    override fun leaveGroup() {
        viewModelScope.launch {
            val res = groupUseCase.leaveGroup(group.value?.id ?: return@launch)
            if (res.isSuccessful) {
                if (group.value!!.groupType != GroupType.WEBINAR) {
                    router.onBack()
                } else {
                    loadData(group.value!!.id)
                }
            } else {
                showDefaultErrorMessage(res.errorModel!!.toErrorMessage())
            }
        }
    }

    override fun cancelGroup(reason: String) {
        group.value?.let {
            viewModelScope.launch {
                groupUseCase.cancelGroup(CancelGroupDTO(it.id, reason)).let { response ->
                    if (response.isSuccessful) {
                        loadData(it.id)
                        closeDialog.postValue(true)
                    } else {
                        showDefaultErrorMessage(response.errorModel!!.toErrorMessage())
                    }
                }

            }
        }
    }

    override fun inviteToGroup(reason: List<String>) {

    }

    override fun startGroup() {
        router.navigateToVideoChat(group.value!!.id, group.value!!.groupType!!)
    }

    override fun onAddPaymentClick() {
        router.navigateToAddPaymentMethod()
    }

    override fun onBackPressed() {
        router.onBack()
    }

    override fun onShareInApp() {
        router.navigateToShare(
            group.value!!.id,
            user.value!!,
            SharePostBottomSheetFragment.ShareType.GROUP
        )
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

    override fun block() {
        viewModelScope.launch {
            chatUseCase.blockUser(group.value!!.owner!!.id).let {
                if (it.isSuccessful) {
                    router.onBack()
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

    override fun onUpdatePrivacyClick(isPrivate: Boolean) {
        group.value.let {
            viewModelScope.launch {
                groupUseCase.updateGroup(it!!.id, it.toUpdatePrivacyGroupDTO(isPrivate))
                    .let { response ->
                        if (response.isSuccessful) {
                            loadData(it.id)
                        } else {
                            if (response.errorModel!!.isNotEmpty()) {
                                showDefaultErrorMessage(response.errorModel!!.toErrorMessage())
                            }
                        }
                    }
            }
        }
    }

    override fun removeMember(attendee: AttendeeEntity) {

    }
}