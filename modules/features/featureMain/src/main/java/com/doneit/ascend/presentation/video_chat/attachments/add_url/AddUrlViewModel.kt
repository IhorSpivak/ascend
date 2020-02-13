package com.doneit.ascend.presentation.video_chat.attachments.add_url

import androidx.lifecycle.MutableLiveData
import com.doneit.ascend.presentation.main.base.BaseViewModelImpl
import com.doneit.ascend.presentation.video_chat.attachments.AttachmentsContract
import com.vrgsoft.annotations.CreateFactory
import com.vrgsoft.annotations.ViewModelDiModule
import com.vrgsoft.networkmanager.livedata.SingleLiveEvent

@CreateFactory
@ViewModelDiModule
class AddUrlViewModel : BaseViewModelImpl(), AddUrlContract.ViewModel {
    override val navigation = SingleLiveEvent<AttachmentsContract.Navigation>()
    override val canSave = MutableLiveData<Boolean>()


    override fun backClick() {
        navigation.postValue(AttachmentsContract.Navigation.BACK)
    }

    override fun onAddUrlClick() {
    }

}