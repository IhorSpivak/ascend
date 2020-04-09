package com.doneit.ascend.presentation.main.create_group.calendar_with_time_picker

import android.os.Bundle
import android.text.format.DateFormat
import android.widget.RadioButton
import androidx.core.view.children
import com.doneit.ascend.domain.entity.CalendarDayEntity
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
    private var selectedDay: CalendarDayEntity? = null
    private var selectedDate: Calendar = Calendar.getInstance()

    override fun viewCreated(savedInstanceState: Bundle?) {
        binding.apply {
            model = viewModel
            btnOk.setOnClickListener {
                val builder = StringBuilder()
                builder.append("${selectedDay.toString().take(3)}, ")
                if (DateFormat.is24HourFormat(context)){
                    builder.append(" ${selectedDate.get(Calendar.HOUR_OF_DAY)}:${selectedDate.get(Calendar.MINUTE)}")
                }else{
                    builder.append(" ${selectedDate.get(Calendar.HOUR)}:${selectedDate.get(Calendar.MINUTE)} ${selectedDate.get(Calendar.AM_PM)}")
                }
                viewModel.createGroupModel.selectedDays.add(selectedDay!!)
                viewModel.createGroupModel.webinarSchedule[position].observableField.set(builder.toString())
                viewModel.okWebinarTimeClick(0, selectedDate, position)
            }
            newWheelPicker.addOnDateChangedListener(object : SingleDateAndTimePicker.OnDateChangedListener{
                override fun onDateChanged(displayed: String?, date: Date?) {
                    selectedDate.time = date
                }
            })
        }
        binding.executePendingBindings()

        viewModel.createGroupModel.getStartTimeDay()?.let {
            //this day mustn't be unselected
            val dayView = getCorrespondingButton(it)
            dayView?.isChecked = true
            dayView?.isEnabled = false
        }
        viewModel.createGroupModel.selectedDays.forEach {
            getCorrespondingButton(it)?.isChecked = true
            binding.radioGroupTop.isEnabled
        }

        binding.radioGroupTop.setOnCheckedChangeListener { radioGroup, i ->
            radioGroup.indexOfChild(binding.root.findViewById<RadioButton>(i)).let {
                selectedDay = CalendarDayEntity.values()[it]
            }
        }
        hideKeyboard()
    }

    private fun getCorrespondingButton(day: CalendarDayEntity): RadioButton? {
        return binding.radioGroupTop.children.elementAtOrNull(day.ordinal) as RadioButton?
    }
}