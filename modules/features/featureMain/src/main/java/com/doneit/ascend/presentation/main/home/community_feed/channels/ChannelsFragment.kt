package com.doneit.ascend.presentation.main.home.community_feed.channels

import android.os.Bundle
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.doneit.ascend.domain.entity.chats.ChatEntity
import com.doneit.ascend.presentation.common.RvLazyAdapter
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.base.BaseFragment
import com.doneit.ascend.presentation.main.common.gone
import com.doneit.ascend.presentation.main.databinding.FragmentChannelsBinding
import com.doneit.ascend.presentation.main.home.channels.dialog.ChannelsDialogInfo
import com.doneit.ascend.presentation.main.home.community_feed.channels.common.ChannelAdapter
import com.doneit.ascend.presentation.utils.extensions.visible
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.dialog_bottom_sheet_channels.view.*
import kotlinx.android.synthetic.main.fragment_my_chats.*
import org.kodein.di.generic.instance

class ChannelsFragment : BaseFragment<FragmentChannelsBinding>() {
    override val viewModelModule = ChannelsViewModelModule.get(this)
    override val viewModel: ChannelsContract.ViewModel by instance()

    private val initChannelAdapter: ChannelAdapter by RvLazyAdapter {
        ChannelAdapter {
            handleChatNavigation(it)
        } to { binding.rvChats }
    }


    override fun viewCreated(savedInstanceState: Bundle?) {
        initChannelAdapter

        binding.apply {
            model = viewModel
            btnBack.setOnClickListener {
                viewModel.onBackPressed()
            }
            tvNewChanel.setOnClickListener {
                viewModel.onNewChannelPressed()
            }
            swipeRefresh.setOnRefreshListener {
                swipeRefresh.isRefreshing = true
                viewModel.filterTextAll.postValue(viewModel.filterTextAll.value)
            }
            clearSearch.setOnClickListener {
                tvSearch.text.clear()
                clearSearch.gone()
            }
            tvSearch.doAfterTextChanged {
                viewModel.filterTextAll.value = it.toString()
                emptyList.setText(if (it.isNullOrEmpty()) R.string.no_channels else R.string.no_results)
                clearSearch.visible(it.isNullOrEmpty().not())
            }
        }

        viewModel.channelWithCurrentUser.observe(viewLifecycleOwner, Observer {
            swipeRefresh.isRefreshing = false
            emptyList.visible(it.chat.isNullOrEmpty())
            initChannelAdapter.submitList(it.chat)
        })
    }

    private fun handleChatNavigation(channel: ChatEntity) {
        if (channel.chatOwnerId == viewModel.user.value!!.id) {
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


}