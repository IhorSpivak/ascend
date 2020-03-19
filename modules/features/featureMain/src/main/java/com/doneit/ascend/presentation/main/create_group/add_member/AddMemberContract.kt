package com.doneit.ascend.presentation.main.create_group.add_member

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import com.doneit.ascend.domain.entity.AttendeeEntity
import com.doneit.ascend.domain.entity.SearchEntity
import com.doneit.ascend.presentation.main.base.BaseViewModel
import com.doneit.ascend.presentation.models.PresentationCreateGroupModel

interface AddMemberContract {
    interface ViewModel: BaseViewModel {
        val createGroupModel: PresentationCreateGroupModel
        val searchResult: LiveData<PagedList<AttendeeEntity>>
        val members: MutableLiveData<MutableList<AttendeeEntity>>
        val selectedMembers: MutableList<AttendeeEntity>
        fun onQueryTextChange(query: String)
        fun goBack()
        fun onAdd(member: AttendeeEntity)
        fun onRemove(member: AttendeeEntity)
        fun onInviteClick(email: String)
    }
}