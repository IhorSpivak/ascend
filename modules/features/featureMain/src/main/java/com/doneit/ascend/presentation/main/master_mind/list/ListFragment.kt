package com.doneit.ascend.presentation.main.master_mind.list

import android.os.Bundle
import com.doneit.ascend.presentation.common.TopListDecorator
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.base.argumented.ArgumentedFragment
import com.doneit.ascend.presentation.main.databinding.FragmentMasterMindListBinding
import com.doneit.ascend.presentation.main.master_mind.list.common.ListArgs
import com.doneit.ascend.presentation.main.master_mind.list.common.MasterMindAdapter
import org.kodein.di.generic.instance

class ListFragment : ArgumentedFragment<FragmentMasterMindListBinding, ListArgs>() {

    override val viewModelModule = ListViewModelModule.get(this)
    override val viewModel: ListContract.ViewModel by instance()

    private val adapter: MasterMindAdapter by lazy {
        MasterMindAdapter()
    }

    override fun viewCreated(savedInstanceState: Bundle?) {
        binding.lifecycleOwner = this
        binding.model = viewModel
        binding.adapter = adapter

        val decorator = TopListDecorator(resources.getDimension(R.dimen.mm_list_top_padding).toInt())
        binding.rvMasterMinds.addItemDecoration(decorator)
        binding.srLayout.setOnRefreshListener {
            binding.srLayout.isRefreshing = false
            viewModel.updateData()
        }
    }
}