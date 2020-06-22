package com.doneit.ascend.presentation.main.create_group.calendar_with_time_picker

import android.os.Bundle
import android.widget.RadioButton
import androidx.core.view.children
import com.doneit.ascend.domain.entity.CalendarDayEntity
import com.doneit.ascend.domain.entity.getDefaultCalendar
import com.doneit.ascend.domain.entity.group.GroupEntity
import com.doneit.ascend.presentation.main.base.BaseFragment
import com.doneit.ascend.presentation.main.create_group.CreateGroupHostContract
import com.doneit.ascend.presentation.main.databinding.FragmentWebinarCalendarPickerBinding
import com.doneit.ascend.presentation.utils.extensions.hideKeyboard
import com.doneit.ascend.presentation.utils.extensions.toCalendar
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import java.util.*

class WebinarCalendarPickerFragment(
    private val position: Int,
    private val group: GroupEntity?
) : BaseFragment<FragmentWebinarCalendarPickerBinding>() {
    override val viewModelModule = Kodein.Module(this::class.java.simpleName) {
        bind<WebinarCalendarPickerContact.ViewModel>() with provider {
            instance<CreateGroupHostContract.ViewModel>()
        }
    }
    override val viewModel: WebinarCalendarPickerContact.ViewModel by instance()
    private var selectedDay: Int = -1
    private var selectedDate: Calendar = getDefaultCalendar()

    override fun viewCreated(savedInstanceState: Bundle?) {
        binding.apply {
            model = viewModel
            viewModel.updateTimeChooserOk(false)
            btnOk.setOnClickListener {
                viewModel.okWebinarTimeClick(selectedDay, selectedDate, position)
            }
            newWheelPicker.setCustomLocale(Locale.ENGLISH)
            if (position == 0) {
                viewModel.createGroupModel.startDate.observableField.let {
                    if (it.get()!!.isNotEmpty()) {
                        viewModel.createGroupModel.actualStartTime.time.let {
                            val temp = it.toCalendar()
                            temp.apply {
                                set(
                                    Calendar.HOUR_OF_DAY,
                                    selectedDate.get(Calendar.HOUR_OF_DAY)
                                )
                                set(
                                    Calendar.HOUR,
                                    selectedDate.get(Calendar.HOUR)
                                )
                                set(
                                    Calendar.MINUTE,
                                    selectedDate.get(Calendar.MINUTE)
                                )
                                set(Calendar.ZONE_OFFSET, selectedDate.get(Calendar.ZONE_OFFSET))
                                newWheelPicker.setDefaultDate(this.time)
                            }
                        }
                    }
                }
            } else {
                viewModel.createGroupModel.webinarSchedule.getOrNull(position)?.let {
                    if (it.observableField.get()!!.isNotEmpty()) {
                        viewModel.createGroupModel.timeList[position].time.let { date ->
                            selectedDate.time = date
                            newWheelPicker.setDefaultDate(date)
                        }
                    }
                }
            }
            newWheelPicker.addOnDateChangedListener { displayed, date -> selectedDate.time = date }
        }
        binding.executePendingBindings()
        if (position == 0) {
            viewModel.createGroupModel.getStartTimeDay()?.let {
                //this day mustn't be unselected
                selectedDay = it.ordinal + 1
                val dayView = getCorrespondingButton(it)
                binding.radioGroupTop.children.forEach {
                    (it as RadioButton).apply {
                        isClickable = false
                    }
                }
                dayView?.isChecked = true
                viewModel.updateTimeChooserOk(true)
            }
        } else {
            viewModel.createGroupModel.scheduleDays.forEachIndexed { index, day ->
                (binding.radioGroupTop.children.elementAtOrNull(day.ordinal) as RadioButton?)?.apply {
                    if (index != position) {
                        isEnabled = false
                        setTextColor(context.resources.getColor(android.R.color.white))
                    }
                }
            }
            viewModel.createGroupModel.scheduleDays.getOrNull(position)?.let { day ->
                (binding.radioGroupTop.children.elementAtOrNull(day.ordinal) as RadioButton?)?.apply {
                    isChecked = true
                    selectedDay = day.ordinal + 1
                    viewModel.updateTimeChooserOk(true)
                }
            }
        }

        binding.radioGroupTop.setOnCheckedChangeListener { radioGroup, i ->
            selectedDay = radioGroup.indexOfChild(binding.root.findViewById<RadioButton>(i)) + 1
            viewModel.updateTimeChooserOk(true)
        }
        hideKeyboard()
    }

    private fun getCorrespondingButton(day: CalendarDayEntity): RadioButton? {
        return binding.radioGroupTop.children.elementAtOrNull(day.ordinal) as RadioButton?
    }
}