package com.doneit.ascend.presentation.main.create_group.calendar_with_time_picker

import android.os.Bundle
import android.widget.RadioButton
import androidx.core.view.children
import com.doneit.ascend.domain.entity.CalendarDayEntity
import com.doneit.ascend.domain.entity.getDefaultCalendar
import com.doneit.ascend.presentation.main.base.BaseFragment
import com.doneit.ascend.presentation.main.create_group.CreateGroupHostContract
import com.doneit.ascend.presentation.main.databinding.FragmentWebinarCalendarPickerBinding
import com.doneit.ascend.presentation.utils.extensions.hideKeyboard
import com.github.florent37.singledateandtimepicker.SingleDateAndTimePicker
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import java.util.*

class WebinarCalendarPickerFragment(
    private val position: Int
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
            btnOk.setOnClickListener {
                viewModel.okWebinarTimeClick(selectedDay, selectedDate, position)
            }
            if (position < 0) {
                viewModel.createGroupModel.startDate.observableField.let {
                    if (it.get()!!.isNotEmpty()){
                        newWheelPicker.setDefaultDate(viewModel.createGroupModel.actualStartTime.time)
                    }
                }
            }else{
                viewModel.createGroupModel.webinarSchedule.getOrNull(position)?.let {
                    if (it.observableField.get()!!.isNotEmpty()) {
                        viewModel.createGroupModel.timeList[position].time.let { date ->
                            newWheelPicker.setDefaultDate(date)
                        }
                    }
                }
            }
            newWheelPicker.addOnDateChangedListener(object : SingleDateAndTimePicker.OnDateChangedListener{
                override fun onDateChanged(displayed: String?, date: Date?) {
                    selectedDate.time = date
                }
            })
        }
        binding.executePendingBindings()
        if (position< 0){
            viewModel.createGroupModel.getStartTimeDay()?.let {
                //this day mustn't be unselected
                selectedDay = it.ordinal + 1
                val dayView = getCorrespondingButton(it)
                dayView?.isChecked = true
                dayView?.isEnabled = false
            }
            viewModel.createGroupModel.selectedDays.forEach {
                getCorrespondingButton(it)?.isChecked = true
                binding.radioGroupTop.isEnabled
            }
        }

        binding.radioGroupTop.setOnCheckedChangeListener { radioGroup, i ->
            selectedDay = radioGroup.indexOfChild(binding.root.findViewById<RadioButton>(i)) + 1
        }
        hideKeyboard()
    }

    private fun getCorrespondingButton(day: CalendarDayEntity): RadioButton? {
        return binding.radioGroupTop.children.elementAtOrNull(day.ordinal) as RadioButton?
    }
}