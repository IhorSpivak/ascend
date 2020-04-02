package com.doneit.ascend.presentation.main.create_group.date_picker

import android.os.Bundle
import com.aigestudio.wheelpicker.WheelPicker
import com.doneit.ascend.domain.entity.MonthEntity
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.base.BaseFragment
import com.doneit.ascend.presentation.main.create_group.CreateGroupHostContract
import com.doneit.ascend.presentation.main.databinding.FragmentDatePickerBinding
import com.doneit.ascend.presentation.models.GroupType
import com.doneit.ascend.presentation.utils.extensions.hideKeyboard
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider

class DatePickerFragment : BaseFragment<FragmentDatePickerBinding>() {

    override val viewModelModule = Kodein.Module(this::class.java.simpleName) {
        //di should contains corresponding ViewModel from SignUpFragments' module for now
        bind<DatePickerContract.ViewModel>() with provider {
            instance<CreateGroupHostContract.ViewModel>()
        }
    }

    override val viewModel: DatePickerContract.ViewModel by instance()

    override fun viewCreated(savedInstanceState: Bundle?) {
        binding.apply {
            model = viewModel
            background = when(viewModel.createGroupModel.groupType){
                GroupType.SUPPORT -> resources.getColor(R.color.support_color)
                GroupType.INDIVIDUAL -> resources.getColor(R.color.background_dimmed)
                GroupType.MASTER_MIND -> resources.getColor(R.color.background_dimmed)
                GroupType.WEBINAR -> resources.getColor(R.color.background_dimmed)
                else -> resources.getColor(R.color.support_color)
            }
            executePendingBindings()
            monthPicker.data = viewModel.getMonthList() //MonthEntity.values().toList()

            dayPicker.setOnItemSelectedListener { _, data, position ->
                viewModel.setDay(data as Int)
            }

            monthPicker.setOnItemSelectedListener { _, data, position ->
                (data as MonthEntity).let {
                    viewModel.setMonth(it)
                    dayPicker.month = it.toNumeric()
                }
            }

            yearPicker.setOnItemSelectedListener { _, data, position ->
                viewModel.setYear(data as Int)
                dayPicker.year = data
            }

            yearPicker.selectedYear = viewModel.getYear()

            val month = viewModel.getMonth()
            val position = monthPicker.getDataIndex { it == month }
            monthPicker.setSelectedItemPosition(position, false)

            dayPicker.year = viewModel.getYear()
            dayPicker.month = month.toNumeric()
            dayPicker.selectedDay = viewModel.getDay()
        }
        hideKeyboard()
    }

    private fun WheelPicker.getDataIndex(predicate: (Any?) -> Boolean): Int {
        val index = data.indexOfFirst(predicate)

        return if (index >= 0) index else DEFAULT_INDEX
    }

    companion object {
        private const val DEFAULT_INDEX = 0
    }
}