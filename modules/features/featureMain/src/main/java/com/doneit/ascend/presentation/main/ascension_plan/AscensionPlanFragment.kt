package com.doneit.ascend.presentation.main.ascension_plan

import android.content.Context
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
    var listener: MainActivityListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = (context as MainActivityListener).apply {
            setTitle(getString(R.string.ascension_plan))
        }
    }

    override fun onResume() {
        super.onResume()
        listener?.apply {
            setSearchEnabled(false)
            setFilterEnabled(true)
            setChatEnabled(true)
            setTitle(getString(R.string.ascension_plan))
            getUnreadMessageCount()
        }
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

    override fun onDetach() {
        listener = null
        super.onDetach()
    }

}