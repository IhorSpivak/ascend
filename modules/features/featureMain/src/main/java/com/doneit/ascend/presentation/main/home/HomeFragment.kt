package com.doneit.ascend.presentation.main.home

import android.content.Context
import android.os.Bundle
import android.os.Handler
import androidx.lifecycle.Observer
import com.doneit.ascend.presentation.MainActivityListener
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.base.BaseFragment
import com.doneit.ascend.presentation.main.databinding.FragmentHomeBinding
import com.doneit.ascend.presentation.main.home.common.TabAdapter
import org.kodein.di.generic.instance

class HomeFragment : BaseFragment<FragmentHomeBinding>() {

    override val viewModelModule = HomeViewModelModule.get(this)

    override val viewModel: HomeContract.ViewModel by instance()
    var listener: MainActivityListener? = null
    var handler: Handler? = null
    var runnable: Runnable? = null


    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = (context as MainActivityListener).apply {
            setCommunityTitle(getString(R.string.main_title))
        }
    }

    override fun onResume() {
        super.onResume()
        listener?.apply {
            setSearchEnabled(true)
            setFilterEnabled(false)
            setChatEnabled(true)
            setShareEnabled(false)
            getUnreadMessageCount()
        }

    }



    override fun viewCreated(savedInstanceState: Bundle?) {
        binding.model = viewModel
        binding.tlGroups.setupWithViewPager(binding.vpGroups)
        binding.vpGroups.offscreenPageLimit = 3


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
        onTrackNewChatMessage()

    }

    private fun setTitle(community: String?) {
        var title = getString(R.string.main_title)
        community?.let {
            title = it
        }
        listener?.setCommunityTitle(title)
    }

    private fun onTrackNewChatMessage() {
    listener?.apply {
            getUnreadMessageCount()
        }

        handler = Handler()
        runnable = Runnable {
            onTrackNewChatMessage()
        }

        handler!!.postDelayed(runnable, 3000)


    }

    override fun onPause() {
        handler!!.removeCallbacksAndMessages(runnable)
        handler!!.removeMessages(0)
        super.onPause()
    }

    override fun onDetach() {
        listener = null
        super.onDetach()
    }


}
