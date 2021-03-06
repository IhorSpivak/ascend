package com.doneit.ascend.presentation.main.create_group.date_picker

import android.os.Bundle
import com.doneit.ascend.domain.entity.group.GroupType
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.base.BaseFragment
import com.doneit.ascend.presentation.main.create_group.CreateGroupHostContract
import com.doneit.ascend.presentation.main.databinding.FragmentWebinarDatePickerBinding
import com.doneit.ascend.presentation.utils.extensions.hideKeyboard
import com.github.florent37.singledateandtimepicker.SingleDateAndTimePicker
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import java.util.*

class WebinarDatePickerFragment: BaseFragment<FragmentWebinarDatePickerBinding>(){

    override val viewModelModule = Kodein.Module(this::class.java.simpleName) {
        bind<WebinarDatePickerContract.ViewModel>() with provider {
            instance<CreateGroupHostContract.ViewModel>()
        }
    }

    private val selectedDate: Calendar = Calendar.getInstance()

    override val viewModel: WebinarDatePickerContract.ViewModel by instance()

    override fun viewCreated(savedInstanceState: Bundle?) {
        binding.apply {
            model = viewModel
            newWheelPicker.setCustomLocale(Locale.ENGLISH)
            background = when(viewModel.createGroupModel.groupType){
                GroupType.SUPPORT -> resources.getColor(R.color.support_color)
                GroupType.INDIVIDUAL -> resources.getColor(R.color.background_dimmed)
                GroupType.MASTER_MIND -> resources.getColor(R.color.background_dimmed)
                GroupType.WEBINAR -> resources.getColor(R.color.red_webinar_color)
                else -> resources.getColor(R.color.support_color)
            }

            btnDone.setOnClickListener {
                viewModel.okDateSelection(selectedDate)
            }
            newWheelPicker.selectDate(viewModel.createGroupModel.actualStartTime)
            viewModel.createGroupModel.actualStartTime.let {
                selectedDate.time = it.time
                newWheelPicker.setDefaultDate(it.time)
            }
            newWheelPicker.addOnDateChangedListener(object : SingleDateAndTimePicker.OnDateChangedListener{
                override fun onDateChanged(displayed: String?, date: Date?) {
                    selectedDate.time = date
                }
            })
        }
        hideKeyboard()
    }
}