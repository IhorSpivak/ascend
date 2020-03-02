package com.doneit.ascend.presentation.main.create_group.calendar_with_time_picker

import android.os.Bundle
import com.doneit.ascend.presentation.main.base.BaseFragment
import com.doneit.ascend.presentation.main.create_group.CreateGroupHostContract
import com.doneit.ascend.presentation.main.databinding.FragmentCalendarWithTimePickerBinding
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider

class CalendarWithTimePickerFragment : BaseFragment<FragmentCalendarWithTimePickerBinding>() {
    override val viewModelModule = Kodein.Module(this::class.java.simpleName) {
        bind<CalendarWithTimePickerContact.ViewModel>() with provider {
            instance<CreateGroupHostContract.ViewModel>()
        }
    }
    override val viewModel: CalendarWithTimePickerContact.ViewModel by instance()

    override fun viewCreated(savedInstanceState: Bundle?) {

    }
}