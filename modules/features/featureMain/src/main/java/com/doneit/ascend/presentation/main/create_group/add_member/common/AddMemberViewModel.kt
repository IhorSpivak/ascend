package com.doneit.ascend.presentation.main.create_group.add_member.common

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import com.doneit.ascend.domain.entity.AttendeeEntity
import com.doneit.ascend.presentation.main.base.BaseViewModel

interface AddMemberViewModel: BaseViewModel {
    val groupId: MutableLiveData<Long>
    val searchVisibility: MutableLiveData<Boolean>
    val inviteVisibility: MutableLiveData<Boolean>
    val inviteButtonActive: MutableLiveData<Boolean>
    val validQuery: MutableLiveData<String>
    val attendees: MutableLiveData<MutableList<AttendeeEntity>>
    val selectedMembers: MutableList<AttendeeEntity>
    val searchResult: LiveData<PagedList<AttendeeEntity>>
    val members: MutableLiveData<MutableList<AttendeeEntity>>

    fun loadAttendees()
    fun onAdd(member: AttendeeEntity)
    fun onRemove(member: AttendeeEntity)
    fun onInviteClick(email: String)
    fun onQueryTextChange(query: String)
    fun goBack()
    fun onClearClick(member: AttendeeEntity)
}