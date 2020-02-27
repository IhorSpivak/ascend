package com.doneit.ascend.presentation.main.ascension_plan.create_spiritual

import android.os.Bundle
import androidx.lifecycle.Observer
import com.doneit.ascend.presentation.main.ascension_plan.create_spiritual.common.SpiritualStepsAdapter
import com.doneit.ascend.presentation.main.base.BaseFragment
import com.doneit.ascend.presentation.main.databinding.FragmentCreateSpiritualBinding
import com.doneit.ascend.presentation.utils.extensions.hideKeyboard
import org.kodein.di.generic.instance

class CreateSpiritualFragment : BaseFragment<FragmentCreateSpiritualBinding>() {

    override val viewModelModule = CreateSpiritualViewModelModule.get(this)
    override val viewModel: CreateSpiritualContract.ViewModel by instance()

    private val stepsAdapter by lazy { SpiritualStepsAdapter() }

    override fun viewCreated(savedInstanceState: Bundle?) {
        binding.model = viewModel
        binding.rvSteps.adapter = stepsAdapter

        viewModel.steps.observe(viewLifecycleOwner, Observer {
            stepsAdapter.submitList(it)
        })

        binding.btnAdd.setOnClickListener {
            hideKeyboard()
            viewModel.add()
        }
    }
}