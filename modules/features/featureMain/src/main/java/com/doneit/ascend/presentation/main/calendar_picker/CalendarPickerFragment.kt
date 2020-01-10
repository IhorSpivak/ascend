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
import com.doneit.ascend.domain.entity.CalendarDayEntity
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

        val checkedListener = CompoundButton.OnCheckedChangeListener { button, isChecked ->
            when (button.id) {
                R.id.btn_mo -> viewModel.changeDayState(CalendarDayEntity.MONDAY, isChecked)
                R.id.btn_tu -> viewModel.changeDayState(CalendarDayEntity.TUESDAY, isChecked)
                R.id.btn_we -> viewModel.changeDayState(CalendarDayEntity.WEDNESDAY, isChecked)
                R.id.btn_th -> viewModel.changeDayState(CalendarDayEntity.THURSDAY, isChecked)
                R.id.btn_fr -> viewModel.changeDayState(CalendarDayEntity.FRIDAY, isChecked)
                R.id.btn_sa -> viewModel.changeDayState(CalendarDayEntity.SATURDAY, isChecked)
                R.id.btn_su -> viewModel.changeDayState(CalendarDayEntity.SUNDAY, isChecked)
            }
        }

        btn_mo.setOnCheckedChangeListener(checkedListener)
        btn_tu.setOnCheckedChangeListener(checkedListener)
        btn_we.setOnCheckedChangeListener(checkedListener)
        btn_th.setOnCheckedChangeListener(checkedListener)
        btn_fr.setOnCheckedChangeListener(checkedListener)
        btn_sa.setOnCheckedChangeListener(checkedListener)
        btn_su.setOnCheckedChangeListener(checkedListener)

        timeTypePicker.postDelayed(
            {
                timeTypePicker.selectedItemPosition = viewModel.getTimeTypePosition()

                hoursPicker.postDelayed({
                    hoursPicker.selectedItemPosition = viewModel.getHoursPosition()

                    viewModel.setHours(hoursPicker.data[viewModel.getHoursPosition()] as String)
                    viewModel.setMinutes(minutesPicker.data[viewModel.getMinutesPosition()] as String)
                    viewModel.setTimeType(timeTypePicker.data[viewModel.getTimeTypePosition()] as String)
                }, 100)
            }, 100
        )

        minutesPicker.postDelayed({
            minutesPicker.selectedItemPosition = viewModel.getMinutesPosition()
        }, 100)

        btn_mo.postDelayed({
            val selectedDays = viewModel.getSelectedDay()

            selectedDays.forEach {
                when (it) {
                    CalendarDayEntity.SATURDAY -> btn_sa.isChecked = true
                    CalendarDayEntity.FRIDAY -> btn_fr.isChecked = true
                    CalendarDayEntity.THURSDAY -> btn_th.isChecked = true
                    CalendarDayEntity.WEDNESDAY -> btn_we.isChecked = true
                    CalendarDayEntity.TUESDAY -> btn_tu.isChecked = true
                    CalendarDayEntity.MONDAY -> btn_mo.isChecked = true
                    CalendarDayEntity.SUNDAY -> btn_su.isChecked = true
                }
            }
        }, 100)

        hideKeyboard()
    }
}