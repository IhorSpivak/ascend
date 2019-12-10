package com.doneit.ascend.presentation.main.calendar_picker

import android.os.Bundle
import android.widget.CompoundButton
import androidx.lifecycle.ViewModelProvider
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.base.BaseFragment
import com.doneit.ascend.presentation.main.base.CommonViewModelFactory
import com.doneit.ascend.presentation.main.create_group.CreateGroupViewModel
import com.doneit.ascend.presentation.main.databinding.FragmentCalendarPickerBinding
import com.doneit.ascend.presentation.main.extensions.hideKeyboard
import com.doneit.ascend.presentation.main.extensions.vmShared
import com.doneit.ascend.presentation.utils.CalendarDay
import com.doneit.ascend.presentation.utils.CalendarPickerUtil
import com.doneit.ascend.presentation.utils.CalendarPickerUtil.Companion.DEFAULT_TIME_TYPE
import kotlinx.android.synthetic.main.fragment_calendar_picker.*
import org.kodein.di.Kodein
import org.kodein.di.direct
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

class CalendarPickerFragment : BaseFragment<FragmentCalendarPickerBinding>() {

    override val viewModelModule = Kodein.Module(this::class.java.simpleName) {
        bind<ViewModelProvider.Factory>() with singleton { CommonViewModelFactory(kodein.direct) }
        //di should contains corresponding ViewModel from SignUpFragments' module for now
        bind<CalendarPickerContract.ViewModel>() with provider {
            vmShared<CreateGroupViewModel>(
                instance()
            )
        }
    }

    override val viewModel: CalendarPickerContract.ViewModel by instance()

    override fun viewCreated(savedInstanceState: Bundle?) {
        binding.model = viewModel
        binding.executePendingBindings()

        hoursPicker.data =
            CalendarPickerUtil.getHours(CalendarPickerUtil.getHours(DEFAULT_TIME_TYPE))
        minutesPicker.data = CalendarPickerUtil.getMinutes()
        timeTypePicker.data = CalendarPickerUtil.getTimeType()

        viewModel.setHours("00")
        viewModel.setMinutes("00")
        viewModel.setTimeType(DEFAULT_TIME_TYPE)

        hoursPicker.setOnItemSelectedListener { _, data, _ ->
            viewModel.setHours(data as String)
        }

        minutesPicker.setOnItemSelectedListener { _, data, _ ->
            viewModel.setMinutes(data as String)
        }

        timeTypePicker.setOnItemSelectedListener { _, data, _ ->
            viewModel.setTimeType(data as String)

            hoursPicker.data =
                CalendarPickerUtil.getHours(CalendarPickerUtil.getHours(data as String))
        }

        val checkedListener = CompoundButton.OnCheckedChangeListener { button, isChecked ->
            when (button.id) {
                R.id.btn_mo -> viewModel.changeDayState(CalendarDay.MONDAY, isChecked)
                R.id.btn_tu -> viewModel.changeDayState(CalendarDay.TUESDAY, isChecked)
                R.id.btn_we -> viewModel.changeDayState(CalendarDay.WEDNESDAY, isChecked)
                R.id.btn_th -> viewModel.changeDayState(CalendarDay.THURSDAY, isChecked)
                R.id.btn_fr -> viewModel.changeDayState(CalendarDay.FRIDAY, isChecked)
                R.id.btn_sa -> viewModel.changeDayState(CalendarDay.SATURDAY, isChecked)
                R.id.btn_su -> viewModel.changeDayState(CalendarDay.SUNDAY, isChecked)
            }
        }

        btn_mo.setOnCheckedChangeListener(checkedListener)
        btn_tu.setOnCheckedChangeListener(checkedListener)
        btn_we.setOnCheckedChangeListener(checkedListener)
        btn_th.setOnCheckedChangeListener(checkedListener)
        btn_fr.setOnCheckedChangeListener(checkedListener)
        btn_sa.setOnCheckedChangeListener(checkedListener)
        btn_sa.setOnCheckedChangeListener(checkedListener)
        btn_su.setOnCheckedChangeListener(checkedListener)

        hideKeyboard()
    }
}