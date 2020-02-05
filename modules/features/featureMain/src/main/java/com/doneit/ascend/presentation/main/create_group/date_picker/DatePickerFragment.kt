package com.doneit.ascend.presentation.main.create_group.date_picker

import android.os.Bundle
import com.doneit.ascend.domain.entity.MonthEntity
import com.doneit.ascend.presentation.main.base.BaseFragment
import com.doneit.ascend.presentation.main.create_group.CreateGroupViewModel
import com.doneit.ascend.presentation.main.databinding.FragmentDatePickerBinding
import com.doneit.ascend.presentation.utils.extensions.hideKeyboard
import com.doneit.ascend.presentation.utils.extensions.vmShared
import kotlinx.android.synthetic.main.fragment_date_picker.*
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import java.util.*

class DatePickerFragment : BaseFragment<FragmentDatePickerBinding>() {

    override val viewModelModule = Kodein.Module(this::class.java.simpleName) {
        //di should contains corresponding ViewModel from SignUpFragments' module for now
        bind<DatePickerContract.ViewModel>() with provider {
            vmShared<CreateGroupViewModel>(
                instance()
            )
        }
    }

    override val viewModel: DatePickerContract.ViewModel by instance()

    override fun viewCreated(savedInstanceState: Bundle?) {
        binding.model = viewModel
        binding.executePendingBindings()

        val cal = Calendar.getInstance()

        dayPicker.selectedDay = cal.get(Calendar.DAY_OF_MONTH)

        viewModel.setDay(dayPicker.selectedDay)
        viewModel.setYear(yearPicker.selectedYear)

        monthPicker.clearAnimation()
        monthPicker.data = viewModel.getMonthList() //MonthEntity.values().toList()

        dayPicker.year = viewModel.getYear()

        dayPicker.setOnItemSelectedListener { _, data, position ->
            viewModel.setDay(data as Int)
            viewModel.setDayPosition(position)
        }

        monthPicker.setOnItemSelectedListener { _, data, position ->
            val month = (data as MonthEntity)

            viewModel.setMonth(month)
            dayPicker.month = month.toNumeric()
        }

        yearPicker.setOnItemSelectedListener { _, data, position ->
            viewModel.setYear(data as Int)
            viewModel.setYearPosition(position)

            dayPicker.year = data
        }

        if (viewModel.getYearPosition() == 0) {
            yearPicker.selectedYear = Calendar.getInstance().get(Calendar.YEAR)
            viewModel.setYear(yearPicker.selectedYear)
        } else {
            yearPicker.selectedItemPosition = viewModel.getYearPosition()
            viewModel.setYear(yearPicker.data[yearPicker.selectedItemPosition] as Int)
        }

        monthPicker.postDelayed({

            val month = viewModel.getMonth()
            val position = monthPicker.data.indexOf(month)
            monthPicker.setSelectedItemPosition(position, false)
            dayPicker.month = month.toNumeric()

            if (viewModel.getDayPosition() == -1) {
                val calDay = cal.get(Calendar.DAY_OF_MONTH)
                viewModel.setDay(dayPicker.data[calDay - 1] as Int)
            } else {
                dayPicker.selectedItemPosition = viewModel.getDayPosition()
                viewModel.setDay(dayPicker.data[viewModel.getDayPosition()] as Int)
            }

        }, 1)

        hideKeyboard()
    }
}