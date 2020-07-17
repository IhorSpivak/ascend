package com.doneit.ascend.presentation.main.home

import android.content.Context
import android.os.Bundle
import androidx.lifecycle.Observer
import com.doneit.ascend.presentation.MainActivityListener
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.base.BaseFragment
import com.doneit.ascend.presentation.main.databinding.FragmentHomeBinding
import com.doneit.ascend.presentation.main.home.common.TabAdapter
import org.kodein.di.generic.instance
import java.util.*

class HomeFragment : BaseFragment<FragmentHomeBinding>() {

    override val viewModelModule = HomeViewModelModule.get(this)
    override val viewModel: HomeContract.ViewModel by instance()
    var listener: MainActivityListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = (context as MainActivityListener).apply {
            setTitle(getString(R.string.main_title), true)
        }
    }

    override fun onResume() {
        super.onResume()
        listener?.apply {
            setSearchEnabled(true)
            setFilterEnabled(false)
            setChatEnabled(true)
            getUnreadMessageCount()
        }

    }

    override fun viewCreated(savedInstanceState: Bundle?) {
        binding.model = viewModel
        binding.tlGroups.setupWithViewPager(binding.vpGroups)
        binding.vpGroups.offscreenPageLimit = 0

        viewModel.user.observe(this, Observer {
            it ?: return@Observer
            setTitle(it.community)

            binding.vpGroups.adapter = TabAdapter.newInstance(
                childFragmentManager,
                it,
                viewModel.getListOfTitles().map {
                    getString(it)
                }
            )
        })
    }

    private fun setTitle(community: String?) {
        var title = getString(R.string.main_title)
        community?.let {
            title = " $community".toUpperCase(Locale.ROOT)
        }
        listener?.setTitle(title, true)
    }

    override fun onDetach() {
        listener = null
        super.onDetach()
    }
}
