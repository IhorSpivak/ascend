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

        yearPicker.selectedYear = Calendar.getInstance().get(Calendar.YEAR)
        dayPicker.selectedDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        monthPicker.setSelectedItemPosition(Calendar.getInstance().get(Calendar.MONTH), true)

        viewModel.setDay(dayPicker.selectedDay)
        viewModel.setMonth(1)
        viewModel.setYear(yearPicker.selectedYear)

        monthPicker.data = datePickerUtil.getMonthList()

        dayPicker.setOnItemSelectedListener { _, data, _ ->
            viewModel.setDay(data as Int)
        }

        monthPicker.setOnItemSelectedListener { _, data, _ ->
            val month = datePickerUtil.getNumberValue(data as String)

            viewModel.setMonth(month)
            dayPicker.month = month
        }

        yearPicker.setOnItemSelectedListener { _, data, _ ->
            viewModel.setYear(data as Int)
        }

        hideKeyboard()
    }
}