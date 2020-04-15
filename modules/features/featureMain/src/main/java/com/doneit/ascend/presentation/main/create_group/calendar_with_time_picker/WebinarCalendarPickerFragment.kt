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
            viewModel.updateTimeChooserOk(false)
            btnOk.setOnClickListener {
                viewModel.okWebinarTimeClick(selectedDay, selectedDate, position)
            }
            if (position == 0) {
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
            newWheelPicker.addOnDateChangedListener { displayed, date -> selectedDate.time = date }
        }
        binding.executePendingBindings()
        viewModel.createGroupModel.scheduleDays.forEachIndexed { index, day ->
            (binding.radioGroupTop.children.elementAtOrNull(day.ordinal) as RadioButton?)?.apply {
                isEnabled = false
            }
        }
        if (position == 0){
            viewModel.createGroupModel.getStartTimeDay()?.let {
                //this day mustn't be unselected
                selectedDay = it.ordinal + 1
                val dayView = getCorrespondingButton(it)
                binding.radioGroupTop.children.forEach {
                    (it as RadioButton).apply {
                        isEnabled = false
                    }
                }
                dayView?.isChecked = true
                viewModel.updateTimeChooserOk(true)
            }
            viewModel.createGroupModel.selectedDays.forEach {
                getCorrespondingButton(it)?.isChecked = true
            }
        }else{
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