package com.doneit.ascend.presentation.main.create_group.calendar_with_time_picker

import android.os.Bundle
import android.util.Log
import androidx.core.util.rangeTo
import com.doneit.ascend.presentation.main.base.BaseFragment
import com.doneit.ascend.presentation.main.create_group.CreateGroupHostContract
import com.doneit.ascend.presentation.main.databinding.FragmentCalendarWithTimePickerBinding
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import java.text.SimpleDateFormat
import java.util.*

class CalendarWithTimePickerFragment : BaseFragment<FragmentCalendarWithTimePickerBinding>() {
    override val viewModelModule = Kodein.Module(this::class.java.simpleName) {
        bind<CalendarWithTimePickerContact.ViewModel>() with provider {
            instance<CreateGroupHostContract.ViewModel>()
        }
    }
    override val viewModel: CalendarWithTimePickerContact.ViewModel by instance()

    override fun viewCreated(savedInstanceState: Bundle?) {
    }
    /*val calendar = Calendar.getInstance()
        val days = calendar.time
        val daysInYear = calendar.getActualMaximum(DAY_OF_YEAR)
        val daysMap = linkedMapOf<String, Calendar>()
        val today = calendar.get(DAY_OF_YEAR)
        for (i in 1..daysInYear){
            calendar.set(DAY_OF_YEAR, i)
            daysMap += if(i == today){
                "Today" to (calendar.clone() as Calendar)
            }else{
                SimpleDateFormat("EEE MMMM dd").format(calendar.time) to (calendar.clone() as Calendar)
            }
        }
        daysMap.keys.forEach {
            Log.d("date ", it)
        }*/
}