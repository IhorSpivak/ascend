package com.doneit.ascend.presentation.main.group_info

import androidx.lifecycle.LiveData
import com.doneit.ascend.domain.entity.group.GroupEntity
import com.doneit.ascend.presentation.main.base.BaseViewModel
import com.doneit.ascend.presentation.models.PresentationCardModel

interface GroupInfoContract {
    interface ViewModel : BaseViewModel {
        val btnSubscribeVisible: LiveData<Boolean>
        val btnJoinVisible: LiveData<Boolean>
        val btnStartVisible: LiveData<Boolean>
        val btnDeleteVisible: LiveData<Boolean>
        val btnJoinedVisible: LiveData<Boolean>
        val group: LiveData<GroupEntity>
        val cards: LiveData<List<PresentationCardModel>>
        val isBlocked: Boolean

        fun onBackPressed()
        fun loadData(groupId: Long)
        fun subscribe(card: PresentationCardModel)
        fun joinToDiscussion()
        fun startGroup()
        fun deleteGroup()
        fun report(content: String)
        fun onAddPaymentClick()
    }

    interface Router {
        fun onBack()
        fun navigateToAddPaymentMethod()
        fun navigateToVideoChat(groupId: Long)
    }
}