package com.doneit.ascend.presentation.video_chat.in_progress.user_options.notes

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import com.doneit.ascend.domain.entity.dto.UpdateNoteDTO
import com.doneit.ascend.domain.use_case.interactor.group.GroupUseCase
import com.doneit.ascend.presentation.main.base.BaseViewModelImpl
import com.doneit.ascend.presentation.utils.extensions.toErrorMessage
import com.vrgsoft.annotations.CreateFactory
import com.vrgsoft.annotations.ViewModelDiModule
import com.vrgsoft.networkmanager.livedata.SingleLiveEvent
import kotlinx.coroutines.launch


@CreateFactory
@ViewModelDiModule
class NotesViewModel(
    private val groupUseCase: GroupUseCase
) : BaseViewModelImpl(), NotesContract.ViewModel {

    private val groupId = MutableLiveData<Long>()
    override val groupInfo = groupId.switchMap { groupUseCase.getGroupDetailsLive(it) }
    override val navigation = SingleLiveEvent<NotesContract.Navigation>()

    override fun init(groupId: Long) {
        this.groupId.postValue(groupId)
    }

    override fun update(newContent: String) {
        groupId.value?.let {
            viewModelScope.launch {
                val result = groupUseCase.updateNote(UpdateNoteDTO(it, newContent))

                if (result.isSuccessful) {
                    onBackClick()
                } else {
                    showDefaultErrorMessage(result.errorModel!!.toErrorMessage())
                }
            }
        }
    }

    override fun onBackClick() {
        navigation.postValue(NotesContract.Navigation.BACK)
    }
}