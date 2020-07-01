package com.doneit.ascend.presentation.main.create_group.add_member.common

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import com.doneit.ascend.domain.entity.AttendeeEntity
import com.doneit.ascend.domain.entity.ParticipantEntity
import com.doneit.ascend.domain.entity.group.GroupEntity
import com.doneit.ascend.presentation.main.base.BaseViewModel

interface AddMemberViewModel: BaseViewModel {
    val group: MutableLiveData<GroupEntity>
    val searchVisibility: MutableLiveData<Boolean>
    val inviteVisibility: MutableLiveData<Boolean>
    val inviteButtonActive: MutableLiveData<Boolean>
    val validQuery: MutableLiveData<String>
    val attendees: MutableLiveData<MutableList<AttendeeEntity>>
    val selectedMembers: MutableList<AttendeeEntity>
    val searchResult: LiveData<PagedList<AttendeeEntity>>
    val members: MutableLiveData<MutableList<AttendeeEntity>>
    val canAddMembers: MutableLiveData<Boolean>
    val users: MutableLiveData<List<ParticipantEntity>>

    fun clearSearchResult()
    fun loadAttendees()
    fun onAdd(member: AttendeeEntity)
    fun onRemove(member: AttendeeEntity)
    fun onInviteClick(emails: List<String>)
    fun onBackClick(emails: List<String>)
    fun onQueryTextChange(query: String)
    fun goBack()
    fun onClearClick(member: AttendeeEntity)
}