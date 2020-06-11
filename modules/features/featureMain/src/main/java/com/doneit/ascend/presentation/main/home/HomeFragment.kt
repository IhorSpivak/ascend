package com.doneit.ascend.presentation.main.home

import android.content.Context
import android.os.Bundle
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.doneit.ascend.presentation.MainActivityListener
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.base.BaseFragment
import com.doneit.ascend.presentation.main.databinding.FragmentHomeBinding
import com.doneit.ascend.presentation.main.home.common.TabAdapter
import com.google.android.material.tabs.TabLayout
import org.kodein.di.generic.instance

class HomeFragment : BaseFragment<FragmentHomeBinding>() {

    override val viewModelModule = HomeViewModelModule.get(this)
    override val viewModel: HomeContract.ViewModel by instance()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val listener = (context as MainActivityListener)
        listener.setTitle(getString(R.string.main_title))
        listener.setSearchEnabled(true)
        listener.setFilterEnabled(false)
        listener.setChatEnabled(true)
    }

    override fun viewCreated(savedInstanceState: Bundle?) {
        binding.model = viewModel
        binding.tlGroups.setupWithViewPager(binding.vpGroups)
        binding.vpGroups.offscreenPageLimit = 0
        viewModel.user.observe(this, Observer {
            setTitle(it?.community)

            binding.vpGroups.adapter = TabAdapter.newInstance(
                childFragmentManager,
                viewModel.getListOfTitles().map {
                    getString(it)
                }
            )
        })
    }

    private fun TabLayout.disableTab(index: Int) {
        (this.getChildAt(0) as ViewGroup).getChildAt(index).isEnabled = false
        (this.getChildAt(0) as ViewGroup).getChildAt(index).alpha = 0.3f
    }

    private fun setTitle(community: String?) {
        var title = getString(R.string.main_title)
        community?.let {
            title += " $community"
        }

        (activity as MainActivityListener).setTitle(title)
    }
}
