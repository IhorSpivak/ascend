package com.doneit.ascend.presentation.main.calendar_picker

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.doneit.ascend.presentation.main.base.BaseFragment
import com.doneit.ascend.presentation.main.base.CommonViewModelFactory
import com.doneit.ascend.presentation.main.create_group.CreateGroupActivity
import com.doneit.ascend.presentation.main.create_group.CreateGroupViewModel
import com.doneit.ascend.presentation.main.databinding.FragmentCalendarPickerBinding
import com.doneit.ascend.presentation.main.extensions.vmShared
import com.doneit.ascend.presentation.utils.CalendarPickerUtil
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

        hoursPicker.data = CalendarPickerUtil.getHours(CalendarPickerUtil.getHours("AM"))
        minutesPicker.data = CalendarPickerUtil.getMinutes()
        timeTypePicker.data = CalendarPickerUtil.getTimeType()

        hoursPicker.setOnItemSelectedListener { _, data, _ ->
            viewModel.setHours(data as String)
        }

        minutesPicker.setOnItemSelectedListener { _, data, _ ->
            viewModel.setMinutes(data as String)
        }

        timeTypePicker.setOnItemSelectedListener { _, data, _ ->
            hoursPicker.data =
                CalendarPickerUtil.getHours(CalendarPickerUtil.getHours(data as String))
        }
    }

    override fun onResume() {
        super.onResume()

        (activity as CreateGroupActivity).clearBackground()
    }

    override fun onStop() {
        super.onStop()

        (activity as CreateGroupActivity).restoreBackground()
    }
}