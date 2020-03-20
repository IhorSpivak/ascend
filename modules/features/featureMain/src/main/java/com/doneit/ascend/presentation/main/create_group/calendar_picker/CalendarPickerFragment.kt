package com.doneit.ascend.presentation.main.create_group.calendar_picker

import android.os.Bundle
import android.text.format.DateFormat
import android.widget.CompoundButton
import android.widget.ToggleButton
import androidx.core.view.children
import com.aigestudio.wheelpicker.WheelPicker
import com.doneit.ascend.domain.entity.CalendarDayEntity
import com.doneit.ascend.presentation.main.base.BaseFragment
import com.doneit.ascend.presentation.main.create_group.CreateGroupHostContract
import com.doneit.ascend.presentation.main.databinding.FragmentCalendarPickerBinding
import com.doneit.ascend.presentation.utils.CalendarPickerUtil
import com.doneit.ascend.presentation.utils.extensions.*
import kotlinx.android.synthetic.main.template_week_days.*
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import java.util.*

class CalendarPickerFragment : BaseFragment<FragmentCalendarPickerBinding>() {

    override val viewModelModule = Kodein.Module(this::class.java.simpleName) {
        bind<CalendarPickerContract.ViewModel>() with provider {
            instance<CreateGroupHostContract.ViewModel>()
        }
    }

    private var selectedHour: String = ""
    private var selectedMinute: String = ""
    private var selectedAmPm: String = ""

    override val viewModel: CalendarPickerContract.ViewModel by instance()

    override fun viewCreated(savedInstanceState: Bundle?) {
        binding.apply {
            model = viewModel
            if(DateFormat.is24HourFormat(context)){
                is24 = true
            }
            btnOk.setOnClickListener {
                viewModel.okClick(selectedHour, selectedMinute, selectedAmPm)
            }
        }
        binding.executePendingBindings()
        binding.hoursPicker.data = context!!.getHoursByTimeZone()
        binding.minutesPicker.data = CalendarPickerUtil.getMinutes()
        binding.timeTypePicker.data = CalendarPickerUtil.getTimeType()

        binding.hoursPicker.setOnItemSelectedListener { _, data, position ->
            selectedHour = ((data as String).toInt() % 12).toString()
            if(DateFormat.is24HourFormat(context)){
                selectedAmPm = if ((data as String).toInt() > 12){
                    "PM"
                }else{
                    "AM"
                }
            }
        }

        binding.minutesPicker.setOnItemSelectedListener { _, data, position ->
            selectedMinute = data as String
        }

        binding.timeTypePicker.setOnItemSelectedListener { _, data, position ->
            selectedAmPm = data as String
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
            val index = daysContainer.children.indexOf(button)
            viewModel.changeDayState(CalendarDayEntity.values()[index], isChecked)
        }

        btn_mo.setOnCheckedChangeListener(checkedListener)
        btn_tu.setOnCheckedChangeListener(checkedListener)
        btn_we.setOnCheckedChangeListener(checkedListener)
        btn_th.setOnCheckedChangeListener(checkedListener)
        btn_fr.setOnCheckedChangeListener(checkedListener)
        btn_sa.setOnCheckedChangeListener(checkedListener)
        btn_su.setOnCheckedChangeListener(checkedListener)


        binding.pickers.waitForLayout {
            val calendar = Calendar.getInstance()
            val timeTypeIndex =
                binding.timeTypePicker.getAmPmIndex {
                    (it as String) == calendar.get(Calendar.AM_PM).toAmPm() }
            val minutesIndex =
                binding.minutesPicker.getMinuteIndex {
                    (it as String) == calendar.get(Calendar.MINUTE).toTimeString() }
            val hoursIndex =
                binding.hoursPicker.getHourIndex {
                    if(DateFormat.is24HourFormat(context)){
                        (it as String) == calendar.get(Calendar.HOUR_OF_DAY).toTimeString()
                    }else{
                        (it as String) == calendar.get(Calendar.HOUR).toTimeString()
                    }

                }

            binding.timeTypePicker.selectedItemPosition = timeTypeIndex
            binding.minutesPicker.selectedItemPosition = minutesIndex
            binding.hoursPicker.selectedItemPosition = hoursIndex
        }

        hideKeyboard()
    }

    private fun getCorrespondingButton(day: CalendarDayEntity): ToggleButton? {
        return binding.daysContainer.children.elementAtOrNull(day.ordinal) as ToggleButton?
    }

    private fun WheelPicker.getHourIndex(predicate: (Any?) -> Boolean): Int {
        val index = data.indexOfFirst(predicate)
        return if (index >= 0) {
            selectedHour = data[index] as String
            index
        } else {
            selectedHour = data[DEFAULT_INDEX] as String
            DEFAULT_INDEX
        }
    }
    private fun WheelPicker.getMinuteIndex(predicate: (Any?) -> Boolean): Int {
        val index = data.indexOfFirst(predicate)
        return if (index >= 0) {
            selectedMinute = data[index] as String
            index
        } else {
            selectedMinute = data[DEFAULT_INDEX] as String
            DEFAULT_INDEX
        }
    }
    private fun WheelPicker.getAmPmIndex(predicate: (Any?) -> Boolean): Int {
        val index = data.indexOfFirst(predicate)
        return if (index >= 0) {
            selectedAmPm = data[index] as String
            index
        } else {
            selectedAmPm = data[DEFAULT_INDEX] as String
            DEFAULT_INDEX
        }
    }

    companion object {
        private const val DEFAULT_INDEX = 0
    }
}