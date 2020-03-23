package com.doneit.ascend.presentation.main.create_group.date_picker

import com.doneit.ascend.domain.entity.MonthEntity
import com.doneit.ascend.presentation.main.base.BaseViewModel
import com.doneit.ascend.presentation.models.PresentationCreateGroupModel

interface DatePickerContract {
    interface ViewModel : BaseViewModel {
        val createGroupModel: PresentationCreateGroupModel

        fun getMonthList(): List<MonthEntity>
        fun cancelClick()
        fun doneClick()
        fun backDateClick()
        fun setMonth(month: MonthEntity)
        fun getMonth(): MonthEntity
        fun setDay(day: Int)
        fun getDay(): Int
        fun setYear(year: Int)
        fun getYear(): Int
    }
}