package com.doneit.ascend.presentation.main.create_group.meeting_count

import android.os.Bundle
import android.widget.RadioButton
import android.widget.RadioGroup
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.base.BaseFragment
import com.doneit.ascend.presentation.main.create_group.CreateGroupHostContract
import com.doneit.ascend.presentation.main.databinding.FragmentNumberOfMeetingsBinding
import com.doneit.ascend.presentation.models.GroupType
import com.doneit.ascend.presentation.utils.extensions.hideKeyboard
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider

class NumberOfMeetingsFragment: BaseFragment<FragmentNumberOfMeetingsBinding>() {
    override val viewModelModule = Kodein.Module(this::class.java.simpleName) {
        bind<NumberOfMeetingsContract.ViewModel>() with provider {
            instance<CreateGroupHostContract.ViewModel>()
        }
    }

    override val viewModel: NumberOfMeetingsContract.ViewModel by instance()

    override fun viewCreated(savedInstanceState: Bundle?) {
        binding.apply {
            model = viewModel
            radioGroupTop.setOnCheckedChangeListener(topListener)
            radioGroupBottom.setOnCheckedChangeListener(bottomListener)
            btnOk.setOnClickListener {
                fragmentManager?.popBackStack()
            }
        }
        hideKeyboard()
    }
    private val topListener: RadioGroup.OnCheckedChangeListener by lazy{
        RadioGroup.OnCheckedChangeListener { p0, p1 ->
            binding.radioGroupBottom.swap(bottomListener)
            viewModel.createGroupModel.numberOfMeetings.observableField.set(binding.root.findViewById<RadioButton>(p1).text.toString())
            viewModel.meetingsCountOk.postValue(viewModel.createGroupModel.numberOfMeetings.observableField.get() != null)
        }
    }
    private val bottomListener: RadioGroup.OnCheckedChangeListener by lazy{
        RadioGroup.OnCheckedChangeListener { p0, p1 ->
            binding.radioGroupTop.swap(topListener)
            viewModel.createGroupModel.numberOfMeetings.observableField.set(binding.root.findViewById<RadioButton>(p1).text.toString())
            viewModel.meetingsCountOk.postValue(viewModel.createGroupModel.numberOfMeetings.observableField.get() != null)
        }
    }
    private fun RadioGroup.swap(listener: RadioGroup.OnCheckedChangeListener){
        this.apply {
            setOnCheckedChangeListener(null)
            clearCheck()
            setOnCheckedChangeListener(listener)
        }
    }
}