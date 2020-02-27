package com.doneit.ascend.presentation.main.ascension_plan.spiritual_action_steps.list

import android.os.Bundle
import androidx.lifecycle.Observer
import com.doneit.ascend.presentation.main.base.BaseFragment
import com.doneit.ascend.presentation.main.base.argumented.ArgumentedFragment

import com.doneit.ascend.presentation.main.databinding.FragmentSpiritualActionStepsListBinding
import com.doneit.ascend.presentation.main.ascension_plan.spiritual_action_steps.list.common.SpiritualActionListAdapter
import com.doneit.ascend.presentation.main.ascension_plan.spiritual_action_steps.list.common.SpiritualActionListArgs
import org.kodein.di.generic.instance

class SpiritualActionListFragment: BaseFragment<FragmentSpiritualActionStepsListBinding>() {
    override val viewModelModule = SpiritualActionListViewModelModule.get(this)
    override val viewModel: SpiritualActionListContract.ViewModel by instance()

    private val adapter: SpiritualActionListAdapter by lazy {
        SpiritualActionListAdapter(
            {
                viewModel.moveToCompleted(it)
            },
            {
                viewModel.editActionStep(it)
            }
        )
    }

    override fun viewCreated(savedInstanceState: Bundle?) {
        binding.model = viewModel
        binding.recyclerViewActionStepList.adapter = this.adapter
        viewModel.spiritualActionList.observe(this, Observer {
            adapter.submitList(it)
        })
    }

    override fun onResume() {
        super.onResume()
        val args = arguments!!.getParcelable<SpiritualActionListArgs>(ArgumentedFragment.KEY_ARGS)!!
        viewModel.applyArguments(args)
    }

    override fun onStop() {
        super.onStop()
    }
}