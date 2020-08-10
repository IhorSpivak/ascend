package com.doneit.ascend.presentation.main.home.channels

import android.os.Bundle
import androidx.lifecycle.Observer
import com.doneit.ascend.presentation.common.RvLazyAdapter
import com.doneit.ascend.presentation.main.base.BaseFragment
import com.doneit.ascend.presentation.main.databinding.FragmentChannelsTabBinding
import com.doneit.ascend.presentation.main.home.channels.adapter.CommunityAdapter
import com.doneit.ascend.presentation.main.home.community_feed.channels.common.ChannelAdapter
import org.kodein.di.generic.instance

class ChannelsFragment : BaseFragment<FragmentChannelsTabBinding>() {

    override val viewModelModule = ChannelsViewModelModule.get(this)
    override val viewModel: ChannelsContract.ViewModel by instance()

    private val communityAdapter by lazy { CommunityAdapter(
        onCommunitySelect = { community ->

        }
    ) }

    private val channelAdapter by lazy {
        ChannelAdapter {
            //handleChatNavigation(it)
        }
    }

    override fun viewCreated(savedInstanceState: Bundle?) {
        binding.apply {
            model = viewModel
            communityList.adapter = communityAdapter
            channelList.adapter = channelAdapter
        }

        viewModel.apply {
            communityList.observe(viewLifecycleOwner, Observer { communities ->
                communityAdapter.setData(communities)
            })

            channelList.observe(viewLifecycleOwner, Observer { channelList ->
                channelAdapter.submitList(channelList)
            })

            fetchCommunityList()
        }
    }

    companion object {

        fun getInstance() = ChannelsFragment()

    }

}