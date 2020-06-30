package com.doneit.ascend.presentation.main.ascension_plan

import android.os.Bundle
import androidx.lifecycle.Observer
import com.doneit.ascend.presentation.MainActivityListener
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.ascension_plan.common.AscensionPlanAdapter
import com.doneit.ascend.presentation.main.base.BaseFragment
import com.doneit.ascend.presentation.main.databinding.FragmentAscensionPlanBinding
import kotlinx.android.synthetic.main.activity_main.*
import org.kodein.di.generic.instance

class AscensionPlanFragment : BaseFragment<FragmentAscensionPlanBinding>() {
    override val viewModelModule = AscensionPlanViewModelModule.get(this)
    override val viewModel: AscensionPlanContract.ViewModel by instance()

    private val adapter = AscensionPlanAdapter()

    override fun onResume() {
        super.onResume()
        val listener = (context as MainActivityListener)
        listener.setTitle(getString(R.string.ascension_plan))
        listener.setSearchEnabled(false)
        listener.setFilterEnabled(true)
        listener.setChatEnabled(true)
    }

    override fun viewCreated(savedInstanceState: Bundle?) {
        binding.model = viewModel
        binding.rvItems.adapter = adapter

        //todo any better solution to set listener?
        activity!!.btnFilter.setOnClickListener {
            viewModel.filter.value?.let { filter ->
                FilterDialog.create(context!!, filter.copy()) {
                    viewModel.setFilterModel(it)
                }.show()
            }
        }

        binding.arrowSaSteps.setOnClickListener {
            viewModel.goToSA()
        }
        binding.arrowPlanSteps.setOnClickListener {
            viewModel.goToG()
        }

        viewModel.data.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })
    }

}