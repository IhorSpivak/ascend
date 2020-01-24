package com.doneit.ascend.presentation.main.create_group.calendar_picker

import android.os.Bundle
import android.widget.CompoundButton
import android.widget.ToggleButton
import androidx.core.view.children
import androidx.lifecycle.ViewModelProvider
import com.doneit.ascend.domain.entity.CalendarDayEntity
import com.doneit.ascend.presentation.main.base.BaseFragment
import com.doneit.ascend.presentation.main.base.CommonViewModelFactory
import com.doneit.ascend.presentation.main.create_group.CreateGroupViewModel
import com.doneit.ascend.presentation.main.databinding.FragmentCalendarPickerBinding
import com.doneit.ascend.presentation.utils.extensions.hideKeyboard
import com.doneit.ascend.presentation.utils.extensions.vmShared
import com.doneit.ascend.presentation.utils.CalendarPickerUtil
import com.doneit.ascend.presentation.utils.extensions.waitForLayout
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

        hoursPicker.data = CalendarPickerUtil.getHours()
        minutesPicker.data = CalendarPickerUtil.getMinutes()
        timeTypePicker.data = CalendarPickerUtil.getTimeType()

        hoursPicker.setOnItemSelectedListener { _, data, position ->
            viewModel.setHours(data as String)
            viewModel.setHoursPosition(position)
        }

        minutesPicker.setOnItemSelectedListener { _, data, position ->
            viewModel.setMinutes(data as String)
            viewModel.setMinutesPosition(position)
        }

        timeTypePicker.setOnItemSelectedListener { _, data, position ->
            viewModel.setTimeType(data as String)
            viewModel.setTimeTypePosition(position)
        }

        val selectedDays = viewModel.createGroupModel.selectedDays
        selectedDays.forEach {
            getCorrespondingButton(it)?.isChecked = true
        }

        viewModel.createGroupModel.getStartTimeDay()?.let {
            //this day mustn't be unselected
            val dayView = getCorrespondingButton(it)
            dayView?.isChecked = true
            dayView?.isEnabled = false
        }


        val checkedListener = CompoundButton.OnCheckedChangeListener { button, isChecked ->
            val index = binding.daysContainer.children.indexOf(button)
            viewModel.changeDayState(CalendarDayEntity.values()[index], isChecked)
        }

        btn_mo.setOnCheckedChangeListener(checkedListener)
        btn_tu.setOnCheckedChangeListener(checkedListener)
        btn_we.setOnCheckedChangeListener(checkedListener)
        btn_th.setOnCheckedChangeListener(checkedListener)
        btn_fr.setOnCheckedChangeListener(checkedListener)
        btn_sa.setOnCheckedChangeListener(checkedListener)
        btn_su.setOnCheckedChangeListener(checkedListener)


        timeTypePicker.postDelayed({
            timeTypePicker.selectedItemPosition = viewModel.getTimeTypePosition()

            hoursPicker.selectedItemPosition = viewModel.getHoursPosition()

            viewModel.setHours(hoursPicker.data[viewModel.getHoursPosition()] as String)
            viewModel.setMinutes(minutesPicker.data[viewModel.getMinutesPosition()] as String)
            viewModel.setTimeType(timeTypePicker.data[viewModel.getTimeTypePosition()] as String)

            minutesPicker.selectedItemPosition = viewModel.getMinutesPosition()
        }, 100)

        hideKeyboard()
    }

    private fun getCorrespondingButton(day: CalendarDayEntity): ToggleButton? {
        return binding.daysContainer.children.elementAtOrNull(day.ordinal) as ToggleButton?
    }
}