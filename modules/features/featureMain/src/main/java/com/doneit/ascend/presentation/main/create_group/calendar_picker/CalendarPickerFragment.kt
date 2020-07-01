package com.doneit.ascend.presentation.main.create_group.calendar_picker

import android.os.Bundle
import android.text.format.DateFormat
import android.view.View
import android.widget.CompoundButton
import android.widget.RadioButton
import android.widget.ToggleButton
import androidx.core.view.children
import com.aigestudio.wheelpicker.WheelPicker
import com.doneit.ascend.domain.entity.CalendarDayEntity
import com.doneit.ascend.presentation.main.base.BaseFragment
import com.doneit.ascend.presentation.main.create_group.CreateGroupHostContract
import com.doneit.ascend.presentation.main.databinding.FragmentCalendarPickerBinding
import com.doneit.ascend.presentation.utils.CalendarPickerUtil
import com.doneit.ascend.presentation.utils.extensions.*
import kotlinx.android.synthetic.main.fragment_calendar_picker.*
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
    private var selectedHourOfDay: String = ""
    private var selectedMinute: String = ""
    private var selectedAmPm: String = ""
    private var previousStates = BooleanArray(7) { false }

    override val viewModel: CalendarPickerContract.ViewModel by instance()

    override fun viewCreated(savedInstanceState: Bundle?) {
        binding.apply {
            model = viewModel
            if (DateFormat.is24HourFormat(context)) {
                is24 = true
            }
            btnOk.setOnClickListener {
                viewModel.okClick(selectedHour, selectedHourOfDay, selectedMinute, selectedAmPm)
            }
        }
        binding.executePendingBindings()
        binding.hoursPicker.data = context!!.getHoursByTimeZone()
        binding.minutesPicker.data = CalendarPickerUtil.getMinutes()
        binding.timeTypePicker.data = CalendarPickerUtil.getTimeType()

        //todo refactor handling time with strings
        binding.hoursPicker.setOnItemSelectedListener { _, data, position ->
            if (DateFormat.is24HourFormat(context)) {
                selectedAmPm = if ((data as String).toInt() > 12) {
                    selectedHour = (data.toInt() - 12).toString()
                    selectedHourOfDay = data
                    "PM"
                } else {
                    selectedHour = data
                    selectedHourOfDay = data
                    "AM"
                }
            } else {
                selectedHour = data as String
                selectedHourOfDay = if (selectedAmPm == "PM") {
                    (data.toInt() + 12).toString()
                } else {
                    data
                }
            }
        }

        binding.minutesPicker.setOnItemSelectedListener { _, data, position ->
            selectedMinute = data as String
        }

        binding.timeTypePicker.setOnItemSelectedListener { _, data, position ->
            selectedAmPm = data as String
            if (!DateFormat.is24HourFormat(context)) {
                selectedHourOfDay = if (selectedAmPm == "PM") {
                    (selectedHourOfDay.toInt() + 12).toString()
                } else {
                    (selectedHourOfDay.toInt() - 12).toString()
                }
            }
        }
        viewModel.createGroupModel.getStartTimeDay()?.let {
            //this day mustn't be unselected
            val dayView = getCorrespondingButton(it)
            dayView?.isChecked = true
            dayView?.isEnabled = false
        }
        viewModel.createGroupModel.selectedDays.forEach {
            getCorrespondingButton(it)?.isChecked = true
        }

        val checkedListener = CompoundButton.OnCheckedChangeListener { button, isChecked ->
            val index = daysContainer.children.indexOf(button)
            viewModel.changeDayState(CalendarDayEntity.values()[index], isChecked)
        }

        val clickListener = View.OnClickListener {
            val index = daysContainer.children.indexOf(it)
            if (it is RadioButton && previousStates[index] == it.isChecked) {
                previousStates[index] = false
                it.isChecked = false
            }
            previousStates[index] = (it as RadioButton).isChecked
        }

        btn_mo.setOnCheckedChangeListener(checkedListener)
        btn_tu.setOnCheckedChangeListener(checkedListener)
        btn_we.setOnCheckedChangeListener(checkedListener)
        btn_th.setOnCheckedChangeListener(checkedListener)
        btn_fr.setOnCheckedChangeListener(checkedListener)
        btn_sa.setOnCheckedChangeListener(checkedListener)
        btn_su.setOnCheckedChangeListener(checkedListener)

        btn_mo.setOnClickListener(clickListener)
        btn_tu.setOnClickListener(clickListener)
        btn_we.setOnClickListener(clickListener)
        btn_th.setOnClickListener(clickListener)
        btn_fr.setOnClickListener(clickListener)
        btn_sa.setOnClickListener(clickListener)
        btn_su.setOnClickListener(clickListener)

        binding.pickers.waitForLayout {
            val calendar = Calendar.getInstance()
            val timeTypeIndex =
                binding.timeTypePicker.getAmPmIndex {
                    if (viewModel.createGroupModel.scheduleTime.observableField.get()!!
                            .isNotBlank()
                    ) {
                        (it as String) == viewModel.createGroupModel.timeType
                    } else {
                        (it as String) == calendar.get(Calendar.AM_PM).toAmPm()
                    }
                }
            val minutesIndex =
                binding.minutesPicker.getMinuteIndex {
                    if (viewModel.createGroupModel.scheduleTime.observableField.get()!!
                            .isNotBlank()
                    ) {
                        (it as String) == viewModel.createGroupModel.minutes
                    } else {
                        (it as String) == calendar.get(Calendar.MINUTE).toTimeString()
                    }
                }
            val hoursIndex =
                binding.hoursPicker.getHourIndex {
                    if (viewModel.createGroupModel.scheduleTime.observableField.get()!!
                            .isNotBlank()
                    ) {
                        if (DateFormat.is24HourFormat(context)) {
                            (it as String) == viewModel.createGroupModel.hoursOfDay.toInt()
                                .toTimeString()
                        } else {
                            (it as String) == viewModel.createGroupModel.hours.toInt()
                                .toTimeString()
                        }

                    } else {
                        if (DateFormat.is24HourFormat(context)) {
                            (it as String) == calendar.get(Calendar.HOUR_OF_DAY).toTimeString()
                        } else {
                            (it as String) == calendar.get(Calendar.HOUR).toTimeString()
                        }
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

    //todo refactor handling time with strings
    private fun WheelPicker.getHourIndex(predicate: (Any?) -> Boolean): Int {
        val index = data.indexOfFirst(predicate)
        return if (index >= 0) {
            if (DateFormat.is24HourFormat(context)) {
                selectedHour = if ((data[index] as String).toInt() > 12) {
                    selectedHourOfDay = data[index] as String
                    ((data[index] as String).toInt() - 12).toString()
                } else {
                    selectedHourOfDay = data[index] as String
                    data[index] as String
                }
            } else {
                selectedHour = data[index] as String
                selectedHourOfDay = if (selectedAmPm == "PM") {
                    ((data[index] as String).toInt() + 12).toString()
                } else {
                    data[index] as String
                }
            }
            index
        } else {
            selectedHour = data[DEFAULT_INDEX] as String
            selectedHourOfDay = data[DEFAULT_INDEX] as String
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