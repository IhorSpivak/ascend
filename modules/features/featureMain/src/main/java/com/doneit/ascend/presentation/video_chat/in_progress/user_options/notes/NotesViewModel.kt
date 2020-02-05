package com.doneit.ascend.presentation.video_chat.in_progress.user_options.notes

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import com.doneit.ascend.domain.entity.group.GroupEntity
import com.doneit.ascend.domain.use_case.interactor.group.GroupUseCase
import com.doneit.ascend.presentation.main.base.BaseViewModelImpl
import com.doneit.ascend.presentation.utils.extensions.toErrorMessage
import com.vrgsoft.annotations.CreateFactory
import com.vrgsoft.annotations.ViewModelDiModule
import kotlinx.coroutines.launch


@CreateFactory
@ViewModelDiModule
class NotesViewModel(
    private val groupUseCase: GroupUseCase,
    private val router: NotesContract.Router
) : BaseViewModelImpl(), NotesContract.ViewModel {

    private val groupId = MutableLiveData<Long>()
    override val groupInfo = groupId.switchMap { groupUseCase.getGroupDetailsLive(it) }

    override fun init(groupId: Long) {
        this.groupId.postValue(groupId)
    }

    override fun onBackClick() {
        router.onBack()
    }
}