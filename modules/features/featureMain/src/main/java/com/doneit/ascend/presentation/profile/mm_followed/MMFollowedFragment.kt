package com.doneit.ascend.presentation.profile.mm_followed

import android.os.Bundle
import androidx.lifecycle.Observer
import com.doneit.ascend.presentation.common.TopListDecorator
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.base.BaseFragment
import com.doneit.ascend.presentation.main.databinding.FragmentMasterMindFollowedBinding
import com.doneit.ascend.presentation.profile.mm_followed.common.FollowedAdapter
import org.kodein.di.generic.instance

class MMFollowedFragment : BaseFragment<FragmentMasterMindFollowedBinding>() {

    override val viewModelModule = MMFollowedViewModelModule.get(this)
    override val viewModel: MMFollowedContract.ViewModel by instance()

    private val adapter: FollowedAdapter by lazy { FollowedAdapter {
        viewModel.unfollow(it)
    }}

    override fun viewCreated(savedInstanceState: Bundle?) {
        val decorator = TopListDecorator(resources.getDimension(R.dimen.groups_list_top_padding).toInt())

        binding.model = viewModel
        binding.lifecycleOwner = this
        binding.content.adapter = adapter
        binding.content.addItemDecoration(decorator)

        binding.addMasterMind.setOnClickListener {
            viewModel.onAddMasterMindClick()
        }

        binding.btnBack.setOnClickListener {

        }

        viewModel.masterMinds.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })
    }

    companion object {
        const val TAG = "MMFollowedFragment"
    }
}
