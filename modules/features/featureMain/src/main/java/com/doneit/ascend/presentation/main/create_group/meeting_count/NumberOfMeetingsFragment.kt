package com.doneit.ascend.presentation.main.create_group.meeting_count

import android.os.Bundle
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.core.view.children
import com.doneit.ascend.presentation.main.base.BaseFragment
import com.doneit.ascend.presentation.main.create_group.CreateGroupHostContract
import com.doneit.ascend.presentation.main.databinding.FragmentNumberOfMeetingsBinding
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

    private var choosenCount = 0

    override val viewModel: NumberOfMeetingsContract.ViewModel by instance()

    override fun viewCreated(savedInstanceState: Bundle?) {
        binding.apply {
            model = viewModel
            viewModel.meetingsCountOk.postValue(choosenCount > 0)
            viewModel.apply {
                if (createGroupModel.numberOfMeetings.observableField.get().toString().isNotEmpty()){
                    choosenCount = createGroupModel.numberOfMeetings.observableField.get().toString().toInt()
                }
            }
            radioGroupBottom.children.forEach {
                if ((it as RadioButton).text == choosenCount.toString()){
                    radioGroupBottom.check(it.id)
                }
            }
            radioGroupTop.children.forEach {
                if ((it as RadioButton).text == choosenCount.toString()){
                    radioGroupTop.check(it.id)
                }
            }
            radioGroupTop.setOnCheckedChangeListener(topListener)
            radioGroupBottom.setOnCheckedChangeListener(bottomListener)
            btnOk.setOnClickListener {
                viewModel.updateNumberOfMeeting(choosenCount)
                fragmentManager?.popBackStack()
            }
        }
        hideKeyboard()
    }
    private val topListener: RadioGroup.OnCheckedChangeListener by lazy{
        RadioGroup.OnCheckedChangeListener { p0, p1 ->
            binding.radioGroupBottom.swap(bottomListener)
            choosenCount = (binding.root.findViewById<RadioButton>(p1).text.toString().toInt())
            viewModel.meetingsCountOk.postValue(choosenCount > 0)
        }
    }
    private val bottomListener: RadioGroup.OnCheckedChangeListener by lazy{
        RadioGroup.OnCheckedChangeListener { p0, p1 ->
            binding.radioGroupTop.swap(topListener)
            choosenCount = (binding.root.findViewById<RadioButton>(p1).text.toString().toInt())
            viewModel.meetingsCountOk.postValue(choosenCount > 0)
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