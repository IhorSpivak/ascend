package com.doneit.ascend.presentation.main.group_info.attendees

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import androidx.paging.PagedList
import com.doneit.ascend.domain.entity.AttendeeEntity
import com.doneit.ascend.domain.entity.ParticipantEntity
import com.doneit.ascend.domain.entity.dto.InviteToGroupDTO
import com.doneit.ascend.domain.entity.group.GroupEntity
import com.doneit.ascend.domain.entity.group.GroupType
import com.doneit.ascend.domain.entity.user.UserEntity
import com.doneit.ascend.domain.use_case.interactor.group.GroupUseCase
import com.doneit.ascend.domain.use_case.interactor.user.UserUseCase
import com.doneit.ascend.presentation.main.base.BaseViewModelImpl
import com.doneit.ascend.presentation.utils.extensions.toErrorMessage
import com.vrgsoft.annotations.CreateFactory
import com.vrgsoft.annotations.ViewModelDiModule
import kotlinx.coroutines.launch

@CreateFactory
@ViewModelDiModule
class AttendeesViewModel (
    private val groupUseCase: GroupUseCase,
    private val userUseCase: UserUseCase,
    private val router: AttendeesContract.Router
) : BaseViewModelImpl(), AttendeesContract.ViewModel {

    private lateinit var currentUser: UserEntity

    init {
        viewModelScope.launch {
            currentUser = userUseCase.getUser()!!
        }
    }

    override val users: MutableLiveData<List<ParticipantEntity>> = MutableLiveData()
    override val group =  MutableLiveData<GroupEntity>()
    override val searchVisibility = MutableLiveData<Boolean>(false)
    override val inviteVisibility = MutableLiveData<Boolean>(false)
    override val inviteButtonActive = MutableLiveData<Boolean>(false)
    override val validQuery = MutableLiveData<String>()
    override val attendees: MutableLiveData<MutableList<AttendeeEntity>> = MutableLiveData()
    override val selectedMembers: MutableList<AttendeeEntity> = mutableListOf()

    override val searchResult: LiveData<PagedList<AttendeeEntity>>
        get() = searchQuery.switchMap {
            groupUseCase.searchMembers(it, currentUser.id)
        }
    override val members = MutableLiveData<MutableList<AttendeeEntity>>()
    override val canAddMembers = MutableLiveData<Boolean>()

    override fun loadAttendees() {
        showProgress(true)
        viewModelScope.launch {
            val response = groupUseCase.getGroupDetails(group.value!!.id)
            val participantsList = groupUseCase.getParticipantList(group.value!!.id)
            if (response.isSuccessful) {
                response.successModel?.attendees?.toMutableList().let {
                    attendees.postValue(it)
                    selectedMembers.clear()
                    checkCanAdd()
                }
            }

            if (participantsList.isSuccessful) {
                users.postValue(participantsList.successModel)
            }
            showProgress(false)
        }
    }

    private val searchQuery =  MutableLiveData<String>()

    override fun onAdd(member: AttendeeEntity) {
        selectedMembers.add(member)
        checkCanAdd()
    }

    override fun onRemove(member: AttendeeEntity) {
        val itemToDelete = selectedMembers.first {
            it.email == member.email
        }
        selectedMembers.remove(itemToDelete)
        checkCanAdd()
    }

    override fun onInviteClick(email: List<String>) {
        group.value?.id!!.let {
            viewModelScope.launch {
                val result = groupUseCase.inviteToGroup(InviteToGroupDTO(it, email))

                if (result.isSuccessful) {
                    searchVisibility.postValue(false)
                    inviteVisibility.postValue(false)
                    loadAttendees()
                } else {
                    showDefaultErrorMessage(result.errorModel!!.toErrorMessage())
                }
            }
        }
    }

    override fun onBackClick(emails: List<String>) {
        group.value?.id!!.let {
            viewModelScope.launch {
                val result = groupUseCase.inviteToGroup(InviteToGroupDTO(it, emails))

                if (result.isSuccessful) {
                    selectedMembers.clear()
                    router.onBack()
                } else {
                    showDefaultErrorMessage(result.errorModel!!.toErrorMessage())
                }
            }
        }
    }

    override fun onQueryTextChange(query: String) {
        if (query.length > 1){
            searchQuery.postValue(query)
            validQuery.postValue(query)
        }
    }

    override fun goBack() {
        router.onBack()
    }

    override fun onClearClick(member: AttendeeEntity) {
        member.let {
            viewModelScope.launch {
                val response = groupUseCase.deleteInvite(group.value!!.id, member.id)

                if (response.isSuccessful) {
                    loadAttendees()
                } else {
                    showDefaultErrorMessage(response.errorModel!!.toErrorMessage())
                }
            }
        }
    }
    private fun checkCanAdd(){
        when(group.value!!.groupType){
            GroupType.INDIVIDUAL -> canAddMembers.postValue((attendees.value?.size?: 0) < 1)
            GroupType.MASTER_MIND -> canAddMembers.postValue(selectedMembers.size + (attendees.value?.size?: 0) < 50)
            GroupType.WEBINAR -> canAddMembers.postValue(selectedMembers.size + (attendees.value?.size?: 0) < 3)
            GroupType.SUPPORT -> canAddMembers.postValue(selectedMembers.size + (attendees.value?.size?: 0) < 50)
        }
    }
}