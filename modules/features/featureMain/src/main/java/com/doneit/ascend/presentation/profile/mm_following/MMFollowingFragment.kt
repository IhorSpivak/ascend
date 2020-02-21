package com.doneit.ascend.presentation.profile.mm_following

import android.os.Bundle
import androidx.lifecycle.Observer
import com.doneit.ascend.presentation.main.base.BaseFragment
import com.doneit.ascend.presentation.main.databinding.FragmentMasterMindFollowingBinding
import com.doneit.ascend.presentation.profile.mm_following.common.FollowingAdapter
import org.kodein.di.generic.instance

class MMFollowingFragment : BaseFragment<FragmentMasterMindFollowingBinding>() {

    override val viewModelModule = MMFollowingViewModelModule.get(this)
    override val viewModel: MMFollowingContract.ViewModel by instance()

    private val adapter: FollowingAdapter by lazy {
        FollowingAdapter({
            viewModel.openInfo(it)
        }, unfollow = {
            viewModel.unfollow(it)
        })
    }

    override fun viewCreated(savedInstanceState: Bundle?) {
        binding.model = viewModel
        binding.content.adapter = adapter

        binding.addMasterMind.setOnClickListener {
            viewModel.onAddMasterMindClick()
        }

        viewModel.masterMinds.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })
    }

    override fun onResume() {
        super.onResume()
        viewModel.refetch()
    }
}
