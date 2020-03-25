package com.doneit.ascend.presentation.main.create_group.price_picker

import androidx.lifecycle.MutableLiveData
import com.doneit.ascend.presentation.main.base.BaseViewModel
import com.doneit.ascend.presentation.models.PresentationCreateGroupModel

interface PricePickerContract {
    interface ViewModel : BaseViewModel {
        val priceOk: MutableLiveData<Boolean>
        val createGroupModel: PresentationCreateGroupModel

        fun backDateClick()
        fun backClick()
        fun okPriceClick(price: String)
        fun setPrice(price: String)
    }
}