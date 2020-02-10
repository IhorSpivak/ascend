package com.doneit.ascend.presentation.main.home

import android.content.Context
import android.os.Bundle
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.doneit.ascend.presentation.MainActivity
import com.doneit.ascend.presentation.MainActivityListener
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.base.BaseFragment
import com.doneit.ascend.presentation.main.base.CommonViewModelFactory
import com.doneit.ascend.presentation.main.databinding.FragmentHomeBinding
import com.doneit.ascend.presentation.main.home.common.MastermindAdapter
import com.doneit.ascend.presentation.main.home.common.TabAdapter
import com.doneit.ascend.presentation.utils.extensions.visible
import com.doneit.ascend.presentation.utils.extensions.vmShared
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.fragment_home.*
import org.kodein.di.Kodein
import org.kodein.di.direct
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

class HomeFragment : BaseFragment<FragmentHomeBinding>() {

    override val viewModelModule = Kodein.Module(this::class.java.simpleName) {
        bind<ViewModelProvider.Factory>(tag = MainActivity.HOME_VM_TAG) with singleton {
            CommonViewModelFactory(
                kodein.direct
            )
        }
        bind<ViewModel>(tag = HomeViewModel::class.java.simpleName) with provider {
            HomeViewModel(
                instance(),
                instance(),
                instance(),
                instance()
            )
        }
        bind<HomeContract.ViewModel>() with provider {
            vmShared<HomeViewModel>(
                instance(tag = MainActivity.HOME_VM_TAG)
            )
        }
    }


    override val viewModel: HomeContract.ViewModel by instance()

    private val mastermindsAdapter: MastermindAdapter by lazy {
        MastermindAdapter(mutableListOf()) {
            viewModel.openProfile(it)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val listener = (context as MainActivityListener)
        listener.setTitle(getString(R.string.main_title))
        listener.setSearchEnabled(true)
    }

    override fun viewCreated(savedInstanceState: Bundle?) {
        binding.model = viewModel
        rvMasterminds.adapter = mastermindsAdapter

        viewModel.user.observe(this, Observer {
            setTitle(it?.community)

            if (it != null) {
                vpGroups.adapter = TabAdapter.newInstance(this, childFragmentManager, it.community)
            }
        })

        viewModel.masterMinds.observe(this, Observer {
            binding.mmPlaceholder.visible(it.isEmpty())
            binding.rvMasterminds.visible(it.isNotEmpty())

            mastermindsAdapter.updateData(it)
        })

        binding.srLayout.setOnRefreshListener {
            binding.srLayout.isRefreshing = false
            viewModel.updateData()
        }

        binding.tlGroups.disableTab(1)
        binding.tlGroups.disableTab(2)
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

    override fun onResume() {
        super.onResume()
        viewModel.updateMasterMinds()
    }
}
