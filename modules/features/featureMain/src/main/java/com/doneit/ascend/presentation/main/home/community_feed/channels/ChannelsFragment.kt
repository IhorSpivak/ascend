package com.doneit.ascend.presentation.main.home.community_feed.channels

import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.PopupMenu
import android.widget.RelativeLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.NestedScrollView
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.doneit.ascend.domain.entity.chats.ChatEntity
import com.doneit.ascend.presentation.common.RvLazyAdapter
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.base.BaseFragment
import com.doneit.ascend.presentation.main.common.gone
import com.doneit.ascend.presentation.main.databinding.FragmentChannelsBinding
import com.doneit.ascend.presentation.main.home.community_feed.channels.common.ChannelAdapter
import com.doneit.ascend.presentation.utils.extensions.visible
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.dialog_bottom_sheet_channels.view.*
import kotlinx.android.synthetic.main.fragment_channels.*
import kotlinx.android.synthetic.main.fragment_my_chats.*
import kotlinx.android.synthetic.main.fragment_my_chats.emptyList
import kotlinx.android.synthetic.main.fragment_my_chats.swipeRefresh
import org.kodein.di.generic.instance

class ChannelsFragment : BaseFragment<FragmentChannelsBinding>() {
    override val viewModelModule = ChannelsViewModelModule.get(this)
    override val viewModel: ChannelsContract.ViewModel by instance()

    private val initChannelAdapter: ChannelAdapter by RvLazyAdapter {
        ChannelAdapter {
           showChannelsDialogInfo(it)
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
            tvNewСhannel.setOnClickListener {
                when(viewModel.user.value?.isMasterMind){
                    true -> showMenu(it)
                    false -> viewModel.onNewChannelPressed()
                }
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
    private fun showChannelsDialogInfo(channel: ChatEntity) {
        val view = layoutInflater.inflate(R.layout.dialog_bottom_sheet_channels, null)
        val dialog = BottomSheetDialog(context!!)
        dialog.setContentView(view)
        view.titleChannel.text = channel.title
        view.user_name.text = channel.owner?.fullName
        view.btn_action.setOnClickListener {
            when(channel.isSubscribed){
                true ->   viewModel.onJoinChannel(channel)
                false ->   viewModel.onLeaveChannel(channel)
            }
        }
        dialog.show()
    }


    private fun showMenu(v: View) {
        PopupMenu(view?.context, v, Gravity.TOP).apply {
            menuInflater.inflate(R.menu.create_new_chat_channels_menu, this.menu)
            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.post_chat -> {
                        viewModel.onNewChatPressed()
                        true
                    }
                    R.id.post_channel -> {
                        viewModel.onNewChannelPressed()
                        true
                    }
                    else -> false
                }
            }
        }.show()
    }


}