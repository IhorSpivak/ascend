package com.doneit.ascend.presentation.main.home

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.doneit.ascend.presentation.main.MainActivity
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.base.BaseFragment
import com.doneit.ascend.presentation.main.base.CommonViewModelFactory
import com.doneit.ascend.presentation.main.databinding.FragmentHomeBinding
import com.doneit.ascend.presentation.main.extensions.vmShared
import com.doneit.ascend.presentation.main.home.common.MastermindAdapter
import com.doneit.ascend.presentation.main.home.common.TabAdapter
import com.doneit.ascend.presentation.utils.extensions.visible
import org.kodein.di.Kodein
import org.kodein.di.direct
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

class HomeFragment : BaseFragment<FragmentHomeBinding>() {

    override val viewModelModule  = Kodein.Module(this::class.java.simpleName) {
        bind<ViewModelProvider.Factory>(tag = MainActivity.HOME_VM_TAG) with singleton { CommonViewModelFactory(kodein.direct) }
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

    private val adapter: TabAdapter by lazy {
        TabAdapter.newInstance(this, childFragmentManager)
    }

    private val mastermindsAdapter: MastermindAdapter by lazy {
        MastermindAdapter(mutableListOf()){
            viewModel.openProfile(it)
        }
    }

    override fun viewCreated(savedInstanceState: Bundle?) {
        binding.lifecycleOwner = this
        binding.model = viewModel
        binding.adapter = adapter
        binding.mastermindAdapter = mastermindsAdapter

        viewModel.user.observe(this, Observer {
            setTitle(it?.community)
        })

        viewModel.masterMinds.observe(this, Observer {
            binding.masterMindPlaceholder.visible(it.isEmpty())
            binding.rvMasterminds.visible(it.isNotEmpty())

            mastermindsAdapter.updateData(it)
        })

        binding.srLayout.setOnRefreshListener {
            binding.srLayout.isRefreshing = false
            viewModel.updateData()
        }

        setTitle(viewModel.user.value?.community)
    }

    override fun onResume() {
        super.onResume()
        viewModel.updateMasterMinds()
    }

    private fun setTitle(community: String?) {
        var title = getString(R.string.main_title)
        community?.let {
            title += " $community"
        }

        binding.tvTitle.text = title
    }
}
