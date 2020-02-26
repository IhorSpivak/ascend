package com.doneit.ascend.presentation.main.ascension_plan.create_goal

import android.os.Bundle
import com.doneit.ascend.presentation.main.base.BaseFragment
import com.doneit.ascend.presentation.main.databinding.FragmentCreateGoalsBinding
import org.kodein.di.generic.instance

class CreateGoalsFragment : BaseFragment<FragmentCreateGoalsBinding>() {

    override val viewModelModule = CreateGoalsViewModelModule.get(this)
    override val viewModel: CreateGoalsContract.ViewModel by instance()

    override fun viewCreated(savedInstanceState: Bundle?) {
        binding.model = viewModel
    }
}