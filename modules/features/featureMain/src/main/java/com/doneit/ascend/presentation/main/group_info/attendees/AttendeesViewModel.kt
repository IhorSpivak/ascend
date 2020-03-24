package com.doneit.ascend.presentation.main.group_info.attendees

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import androidx.paging.PagedList
import com.doneit.ascend.domain.entity.AttendeeEntity
import com.doneit.ascend.domain.entity.dto.CancelGroupDTO
import com.doneit.ascend.domain.entity.dto.InviteToGroupDTO
import com.doneit.ascend.domain.use_case.interactor.group.GroupUseCase
import com.doneit.ascend.domain.use_case.interactor.search.SearchUseCase
import com.doneit.ascend.presentation.main.base.BaseViewModelImpl
import com.doneit.ascend.presentation.main.search.SearchContract
import com.doneit.ascend.presentation.models.ValidatableField
import com.doneit.ascend.presentation.utils.extensions.toErrorMessage
import com.vrgsoft.annotations.CreateFactory
import com.vrgsoft.annotations.ViewModelDiModule
import kotlinx.coroutines.launch

@CreateFactory
@ViewModelDiModule
class AttendeesViewModel (
    private val groupUseCase: GroupUseCase,
    private val router: AttendeesContract.Router
) : BaseViewModelImpl(), AttendeesContract.ViewModel {

    override val groupId: MutableLiveData<Long> = MutableLiveData()
    override val searchVisibility = MutableLiveData<Boolean>(false)
    override val inviteVisibility = MutableLiveData<Boolean>(false)
    override val inviteButtonActive = MutableLiveData<Boolean>(false)
    override val validQuery = MutableLiveData<String>()
    override val attendees: MutableLiveData<MutableList<AttendeeEntity>> = MutableLiveData()
    override val selectedMembers: MutableList<AttendeeEntity> = mutableListOf()

    override val searchResult: LiveData<PagedList<AttendeeEntity>>
        get() = searchQuery.switchMap {
            groupUseCase.searchMembers(it)
        }
    override val members = MutableLiveData<MutableList<AttendeeEntity>>()

    override fun loadAttendees() {
        showProgress(true)
        viewModelScope.launch {
            val response = groupUseCase.getGroupDetails(groupId.value!!)

            if (response.isSuccessful) {
                attendees.postValue(response.successModel?.attendees?.toMutableList())
            }
            showProgress(false)
        }
    }

    private val searchQuery =  MutableLiveData<String>()

    override fun onAdd(member: AttendeeEntity) {
        if (member.email != null && member.email.isNullOrBlank()) {
            if (member.email.isNullOrBlank()){
                selectedMembers.add(member)
            }
        }
    }

    override fun onRemove(member: AttendeeEntity) {
        selectedMembers.remove(member)
    }

    override fun onInviteClick(email: String) {
        groupId.value?.let {
            viewModelScope.launch {
                val result = groupUseCase.inviteToGroup(InviteToGroupDTO(it, listOf(email)))

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
                val response = groupUseCase.deleteInvite(groupId.value!!, member.id)

                if (response.isSuccessful) {
                    loadAttendees()
                } else {
                    showDefaultErrorMessage(response.errorModel!!.toErrorMessage())
                }
            }
        }
    }
}