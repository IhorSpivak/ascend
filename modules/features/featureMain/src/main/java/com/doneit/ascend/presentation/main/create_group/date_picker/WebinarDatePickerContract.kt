package com.doneit.ascend.presentation.main.create_group.date_picker

import com.doneit.ascend.presentation.main.base.BaseViewModel
import com.doneit.ascend.presentation.models.PresentationCreateGroupModel
import java.util.*

interface WebinarDatePickerContract {
    interface ViewModel : BaseViewModel {
        val createGroupModel: PresentationCreateGroupModel

        fun cancelDateSelection()
        fun okDateSelection(date: Calendar)
    }
}