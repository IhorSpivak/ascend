package com.doneit.ascend.presentation.main.ascension_plan

import android.content.Context
import android.os.Bundle
import com.doneit.ascend.presentation.MainActivityListener
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.base.BaseFragment
import com.doneit.ascend.presentation.main.databinding.FragmentAscensionPlanBinding
import org.kodein.di.generic.instance

class AscensionPlanFragment : BaseFragment<FragmentAscensionPlanBinding>() {
    override val viewModelModule = AscensionPlanViewModelModule.get(this)
    override val viewModel: AscensionPlanContract.ViewModel by instance()

    override
    fun viewCreated(savedInstanceState: Bundle?) {
        binding.arrowSaSteps.setOnClickListener {
            viewModel.goToSA()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val listener = (context as MainActivityListener)
        listener.setTitle(getString(R.string.ascension_plan))
        listener.setSearchEnabled(false)
        listener.setFilterEnabled(true)
    }

}