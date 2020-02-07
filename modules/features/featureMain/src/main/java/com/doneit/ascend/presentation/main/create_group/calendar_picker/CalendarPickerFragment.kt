package com.doneit.ascend.presentation.main.create_group.calendar_picker

import android.os.Bundle
import android.widget.CompoundButton
import android.widget.ToggleButton
import androidx.core.view.children
import com.aigestudio.wheelpicker.WheelPicker
import com.doneit.ascend.domain.entity.CalendarDayEntity
import com.doneit.ascend.presentation.main.base.BaseFragment
import com.doneit.ascend.presentation.main.create_group.CreateGroupHostContract
import com.doneit.ascend.presentation.main.databinding.FragmentCalendarPickerBinding
import com.doneit.ascend.presentation.utils.CalendarPickerUtil
import com.doneit.ascend.presentation.utils.extensions.hideKeyboard
import kotlinx.android.synthetic.main.fragment_calendar_picker.*
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider

class CalendarPickerFragment : BaseFragment<FragmentCalendarPickerBinding>() {

    override val viewModelModule = Kodein.Module(this::class.java.simpleName) {
        bind<CalendarPickerContract.ViewModel>() with provider {
            instance<CreateGroupHostContract.ViewModel>()
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
        }

        minutesPicker.setOnItemSelectedListener { _, data, position ->
            viewModel.setMinutes(data as String)
        }

        timeTypePicker.setOnItemSelectedListener { _, data, position ->
            viewModel.setTimeType(data as String)
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
            val timeTypeIndex =
                timeTypePicker.getDataIndex { (it as String) == viewModel.createGroupModel.timeType }
            val minutesIndex =
                minutesPicker.getDataIndex { (it as String) == viewModel.createGroupModel.minutes }
            val hoursIndex =
                hoursPicker.getDataIndex { (it as String) == viewModel.createGroupModel.hours }

            timeTypePicker.selectedItemPosition = timeTypeIndex
            minutesPicker.selectedItemPosition = minutesIndex
            hoursPicker.selectedItemPosition = hoursIndex
        }, 100)

        hideKeyboard()
    }

    private fun getCorrespondingButton(day: CalendarDayEntity): ToggleButton? {
        return binding.daysContainer.children.elementAtOrNull(day.ordinal) as ToggleButton?
    }

    private fun WheelPicker.getDataIndex(predicate: (Any?) -> Boolean): Int {
        val index = data.indexOfFirst(predicate)

        return if (index >= 0) index else DEFAULT_INDEX
    }

    companion object {
        private const val DEFAULT_INDEX = 0
    }
}