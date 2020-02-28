package com.doneit.ascend.presentation.main.ascension_plan.goals.list

import android.os.Bundle
import androidx.lifecycle.Observer
import com.doneit.ascend.presentation.main.ascension_plan.goals.list.common.GoalsListAdapter
import com.doneit.ascend.presentation.main.ascension_plan.goals.list.common.GoalsListArgs
import com.doneit.ascend.presentation.main.base.BaseFragment
import com.doneit.ascend.presentation.main.base.argumented.ArgumentedFragment
import com.doneit.ascend.presentation.main.databinding.FragmentGoalsListBinding
import org.kodein.di.generic.instance

class GoalsListFragment : BaseFragment<FragmentGoalsListBinding>() {
    override val viewModelModule = GoalsListViewModelModule.get(this)
    override val viewModel: GoalsListContract.ViewModel by instance()

    private val adapter: GoalsListAdapter by lazy {
        GoalsListAdapter(
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
        binding.recyclerViewGoalsList.adapter = this.adapter
        viewModel.spiritualActionList.observe(this, Observer {
            adapter.submitList(it)
        })
    }

    override fun onResume() {
        super.onResume()
        val args = arguments!!.getParcelable<GoalsListArgs>(ArgumentedFragment.KEY_ARGS)!!
        viewModel.applyArguments(args)
    }

    override fun onStop() {
        super.onStop()
    }
}