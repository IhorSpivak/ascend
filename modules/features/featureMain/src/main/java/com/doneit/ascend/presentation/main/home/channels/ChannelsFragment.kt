package com.doneit.ascend.presentation.main.home.channels

import android.os.Bundle
import android.os.Parcelable
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.doneit.ascend.domain.entity.chats.ChatEntity
import com.doneit.ascend.domain.entity.user.UserEntity
import com.doneit.ascend.presentation.common.RvLazyAdapter
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.base.BaseFragment
import com.doneit.ascend.presentation.main.databinding.FragmentChannelsTabBinding
import com.doneit.ascend.presentation.main.home.channels.adapter.CommunityAdapter
import com.doneit.ascend.presentation.main.home.channels.dialog.ChannelsDialogInfo
import com.doneit.ascend.presentation.main.home.community_feed.channels.common.ChannelAdapter
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.parcel.Parcelize
import kotlinx.android.synthetic.main.dialog_bottom_sheet_channels.view.*
import org.kodein.di.generic.instance

class ChannelsFragment : BaseFragment<FragmentChannelsTabBinding>() {

    private val args by lazy { arguments?.getParcelable<Args>(EXTRA_ARGS) }

    override val viewModelModule = ChannelsViewModelModule.get(this)
    override val viewModel: ChannelsContract.ViewModel by instance()

    private val communityAdapter by lazy { CommunityAdapter(
        onCommunitySelect = { community ->
            viewModel.fetchChannelsList(community)
        }
    ) }

    private val channelAdapter by lazy {
        ChannelAdapter {
            handleChatNavigation(it)
        }
    }

    override fun viewCreated(savedInstanceState: Bundle?) {
        binding.apply {
            model = viewModel
            communityList.adapter = communityAdapter
            channelList.adapter = channelAdapter

            pullToRefreshChannels.setOnRefreshListener {
                viewModel.fetchChannelsList()
            }
        }

        viewModel.apply {
            communityList.observe(viewLifecycleOwner, Observer { communities ->
                communityAdapter.setData(communities)
            })

            channelList.observe(viewLifecycleOwner, Observer { channelList ->
                binding.pullToRefreshChannels.isRefreshing = false
                channelAdapter.submitList(channelList)
            })

            args?.user?.let { setUser(it) }
            fetchCommunityList()
        }
    }

    private fun handleChatNavigation(channel: ChatEntity) {
        if (channel.chatOwnerId == viewModel.user.value?.id) {
            viewModel.onChannelPressed(channel)
        }
        when (channel.isSubscribed) {
            true -> viewModel.onChannelPressed(channel)
            false -> showChannelsDialogInfo(channel)
        }
    }

    private fun showChannelsDialogInfo(channel: ChatEntity) {
        ChannelsDialogInfo.getInstance(
            channel,
            onJoinChannel = {
                viewModel.onJoinChannel(channel)
            }
        ).let {
            activity?.supportFragmentManager?.let { fragmentManager ->
                it.show(fragmentManager, it.tag)
            }
        }
    }

    companion object {

        private const val EXTRA_ARGS = "EXTRA_ARGS"

        @Parcelize
        private data class Args(
            val user: UserEntity
        ): Parcelable

        fun getInstance(user: UserEntity) = ChannelsFragment().apply {
            arguments = Bundle().apply {
                putParcelable(EXTRA_ARGS, Args(user))
            }
        }

    }

}