package com.doneit.ascend.presentation.main.home.community_feed.channels

import android.os.Bundle
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.Observer
import com.doneit.ascend.domain.entity.chats.ChatEntity
import com.doneit.ascend.presentation.common.RvLazyAdapter
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.base.BaseFragment
import com.doneit.ascend.presentation.main.common.gone
import com.doneit.ascend.presentation.main.databinding.FragmentChannelsBinding
import com.doneit.ascend.presentation.main.home.community_feed.channels.common.ChannelAdapter
import com.doneit.ascend.presentation.utils.extensions.visible
import kotlinx.android.synthetic.main.fragment_my_chats.*
import org.kodein.di.generic.instance

class ChannelsFragment : BaseFragment<FragmentChannelsBinding>() {
    override val viewModelModule = ChannelsViewModelModule.get(this)
    override val viewModel: ChannelsContract.ViewModel by instance()

    private val initChannelAdapter: ChannelAdapter by RvLazyAdapter {
        ChannelAdapter {
            viewModel.onChannelPressed(it)
        } to { binding.rvChats }
    }
    private var lastChecked: ChatEntity? = null

    override fun viewCreated(savedInstanceState: Bundle?) {
        initChannelAdapter
        binding.apply {
            model = viewModel
            btnBack.setOnClickListener {
                viewModel.onBackPressed()
            }
            tvNewChat.setOnClickListener {
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

}