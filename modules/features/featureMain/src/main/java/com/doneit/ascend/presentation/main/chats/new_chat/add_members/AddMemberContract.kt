package com.doneit.ascend.presentation.main.chats.new_chat.add_members

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.doneit.ascend.domain.entity.AttendeeEntity
import com.doneit.ascend.presentation.main.base.BaseViewModel

interface AddMemberContract {
    interface ViewModel : BaseViewModel {
        val searchResult: LiveData<PagedList<AttendeeEntity>>
        val addedMembers: LiveData<List<AttendeeEntity>>
        val canAddMember: Boolean
        val selectedMembers: MutableList<AttendeeEntity>
        fun onBackPressed()
        fun onLocalBackPressed()
        fun onAddMember(member: AttendeeEntity)
        fun onRemoveMember(member: AttendeeEntity)
        fun onQueryTextChange(query: String)
    }

    interface Router {
        fun onBack()
    }
}