package com.doneit.ascend.presentation.main.group_info.attendees

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.doneit.ascend.domain.entity.AttendeeEntity
import com.doneit.ascend.domain.entity.dto.CancelGroupDTO
import com.doneit.ascend.domain.use_case.interactor.group.GroupUseCase
import com.doneit.ascend.domain.use_case.interactor.search.SearchUseCase
import com.doneit.ascend.presentation.main.base.BaseViewModelImpl
import com.doneit.ascend.presentation.main.search.SearchContract
import com.doneit.ascend.presentation.utils.extensions.toErrorMessage
import com.vrgsoft.annotations.CreateFactory
import com.vrgsoft.annotations.ViewModelDiModule
import kotlinx.coroutines.launch

@CreateFactory
@ViewModelDiModule
class AttendeesViewModel (
    private val groupUseCase: GroupUseCase
) : BaseViewModelImpl(), AttendeesContract.ViewModel {
    override val groupId: MutableLiveData<Long> = MutableLiveData()
    override val attendees: MutableLiveData<MutableList<AttendeeEntity>> = MutableLiveData()
    override val selectedMembers: MutableList<AttendeeEntity> = mutableListOf()

    override fun onAdd(member: AttendeeEntity) {
        selectedMembers.add(member)
    }

    override fun onRemove(member: AttendeeEntity) {
        selectedMembers.remove(member)
    }

    override fun onInviteClick(email: String) {
    }

    override fun submitRequest(query: String) {

    }

    override fun goBack() {
    }

    override fun onClearClick(member: AttendeeEntity) {
        member.let {
            viewModelScope.launch {
                val response = groupUseCase.deleteInvite(groupId.value!!, member.id)

                if (response.isSuccessful) {
                    selectedMembers.remove(member)
                    attendees.postValue(selectedMembers.toMutableList())
                } else {
                    showDefaultErrorMessage(response.errorModel!!.toErrorMessage())
                }
            }
        }
    }

    override fun onAttendeeClick() {
    }

}