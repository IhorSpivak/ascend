package com.doneit.ascend.presentation.main.home.community_feed.channels.create_channel.add_members

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import com.doneit.ascend.domain.entity.AttendeeEntity
import com.doneit.ascend.presentation.main.base.BaseViewModel

interface AddMembersContract {
    interface ViewModel : BaseViewModel {
        val searchResult: LiveData<PagedList<AttendeeEntity>>
        val selectedMembers: MutableList<AttendeeEntity>
        val connectionAvailable: MutableLiveData<Boolean>
        val canComplete: LiveData<Boolean>
        val addedMembers: LiveData<List<AttendeeEntity>>
        fun onAddMember(member: AttendeeEntity)
        fun onRemoveMember(member: AttendeeEntity)
        fun onDeleteMember(id: Long)
        fun onBackPressed()
        fun onLocalBackPressed()
        fun onQueryTextChange(query: String)
        fun complete()
    }

    interface Router {
        fun onBack()
    }
}