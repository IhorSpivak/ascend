package com.doneit.ascend.presentation.main.group_info

import androidx.lifecycle.LiveData
import com.doneit.ascend.domain.entity.GroupEntity
import com.doneit.ascend.presentation.main.base.BaseViewModel
import com.doneit.ascend.presentation.models.PresentationCardModel

interface GroupInfoContract {
    interface ViewModel : BaseViewModel {
        val btnSubscribeVisible: LiveData<Boolean>
        val btnJoinVisible: LiveData<Boolean>
        val btnStartVisible: LiveData<Boolean>
        val btnDeleteVisible: LiveData<Boolean>
        val group: LiveData<GroupEntity>
        val cards: LiveData<List<PresentationCardModel>>

        fun onBackPressed()
        fun loadData(groupId: Long)
        fun setModel(model: GroupEntity)
        fun subscribe(card: PresentationCardModel)
        fun joinToDiscussion()
        fun startGroup()
        fun deleteGroup()
        fun report(content: String)
    }

    interface Router {
        fun onBack()
    }
}