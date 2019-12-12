package com.doneit.ascend.presentation.main.date_picker

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.doneit.ascend.presentation.main.base.BaseFragment
import com.doneit.ascend.presentation.main.base.CommonViewModelFactory
import com.doneit.ascend.presentation.main.create_group.CreateGroupViewModel
import com.doneit.ascend.presentation.main.databinding.FragmentDatePickerBinding
import com.doneit.ascend.presentation.main.extensions.hideKeyboard
import com.doneit.ascend.presentation.main.extensions.vmShared
import com.doneit.ascend.presentation.utils.DatePickerUtil
import kotlinx.android.synthetic.main.fragment_date_picker.*
import org.kodein.di.Kodein
import org.kodein.di.direct
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton
import java.util.*

class DatePickerFragment : BaseFragment<FragmentDatePickerBinding>() {

    override val viewModelModule = Kodein.Module(this::class.java.simpleName) {
        bind<ViewModelProvider.Factory>() with singleton { CommonViewModelFactory(kodein.direct) }
        //di should contains corresponding ViewModel from SignUpFragments' module for now
        bind<DatePickerContract.ViewModel>() with provider {
            vmShared<CreateGroupViewModel>(
                instance()
            )
        }
    }

    override val viewModel: DatePickerContract.ViewModel by instance()
    private val datePickerUtil: DatePickerUtil by instance()

    override fun viewCreated(savedInstanceState: Bundle?) {
        binding.model = viewModel
        binding.executePendingBindings()

        dayPicker.selectedDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)


        viewModel.setDay(dayPicker.selectedDay)
        viewModel.setMonth(1)
        viewModel.setYear(yearPicker.selectedYear)

        monthPicker.data = datePickerUtil.getMonthList()

        dayPicker.setOnItemSelectedListener { _, data, position ->
            viewModel.setDay(data as Int)
            viewModel.setDayPosition(position)
        }

        monthPicker.setOnItemSelectedListener { _, data, position ->
            val month = datePickerUtil.getNumberValue(data as String)

            viewModel.setMonth(month)
            viewModel.setMonthPosition(position)
            dayPicker.month = month

        }

        yearPicker.setOnItemSelectedListener { _, data, position ->
            viewModel.setYear(data as Int)
            viewModel.setYearPosition(position)

            dayPicker.year = data
        }

        yearPicker.postDelayed(
            {
                if (viewModel.getYearPosition() == 0) {
                    yearPicker.selectedYear = Calendar.getInstance().get(Calendar.YEAR)
                } else {
                    yearPicker.selectedItemPosition = viewModel.getYearPosition()
                }

                dayPicker.year = viewModel.getYear()
                dayPicker.month = viewModel.getMonth()

                monthPicker.postDelayed({

                    if (viewModel.getMonth() == 0) {
                        monthPicker.setSelectedItemPosition(
                            Calendar.getInstance().get(Calendar.MONTH),
                            true
                        )
                    } else {
                        monthPicker.selectedItemPosition = viewModel.getMonthPosition()
                    }

                    dayPicker.postDelayed({
                        dayPicker.selectedItemPosition = viewModel.getDayPosition()

                        viewModel.setYear(yearPicker.data[viewModel.getYearPosition()] as Int)

                        val month =
                            datePickerUtil.getNumberValue(monthPicker.data[viewModel.getMonthPosition()] as String)
                        viewModel.setMonth(month)
                        viewModel.setDay(dayPicker.data[viewModel.getDayPosition()] as Int)
                    }, 50)
                }, 50)
            },
            100
        )

        hideKeyboard()
    }
}