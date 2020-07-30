package com.doneit.ascend.presentation.main.chats

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.View
import android.widget.PopupMenu
import androidx.lifecycle.Observer
import androidx.paging.PagedList
import androidx.recyclerview.widget.LinearLayoutManager
import com.doneit.ascend.domain.entity.chats.ChatEntity
import com.doneit.ascend.domain.entity.chats.MessageStatus
import com.doneit.ascend.presentation.dialog.BlockUserDialog
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.base.BaseFragment
import com.doneit.ascend.presentation.main.chats.common.MyChatsAdapter
import com.doneit.ascend.presentation.main.common.gone
import com.doneit.ascend.presentation.main.databinding.FragmentMyChatsBinding
import com.doneit.ascend.presentation.utils.extensions.visible
import kotlinx.android.synthetic.main.fragment_my_chats.*
import org.kodein.di.generic.instance

class MyChatsFragment : BaseFragment<FragmentMyChatsBinding>() {
    override val viewModelModule = MyChatsViewModelModule.get(this)
    override val viewModel: MyChatsContract.ViewModel by instance()

    private val adapter: MyChatsAdapter by lazy {
        MyChatsAdapter(
            {
                viewModel.onChatPressed(it)
            }, {
                showDeleteDialog(it)
            }
        )
    }
    private var lastChecked: ChatEntity? = null

    override fun viewCreated(savedInstanceState: Bundle?) {
        binding.apply {
            model = viewModel
            btnBack.setOnClickListener {
                viewModel.onBackPressed()
            }
            rvChats.adapter = adapter
            tvNewChat.setOnClickListener {
                when (viewModel.user.value?.isMasterMind) {
                    true -> showMenu(it)
                    false -> viewModel.onNewChatPressed()
                }
            }
        }

        viewModel.chatsWithCurrentUser.observe(viewLifecycleOwner, Observer {
            swipeRefresh.isRefreshing = false
            emptyList.visible(it.chat.isNullOrEmpty())
            adapter.updateUser(it.user)
            adapter.submitList(it.chat)
            scrollIfNeed(it.chat)
        })
        binding.swipeRefresh.setOnRefreshListener {
            swipeRefresh.isRefreshing = true
            //todo: fix this
            viewModel.filterTextAll.postValue(viewModel.filterTextAll.value)
        }
        binding.clearSearch.setOnClickListener {
            tvSearch.text.clear()
            clearSearch.gone()
        }
        binding.tvSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                viewModel.filterTextAll.value = p0.toString()
                emptyList.setText(if (p0.isNullOrEmpty()) R.string.no_chats else R.string.no_results)
                clearSearch.visible(p0.isNullOrEmpty().not())
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })
    }

    private fun scrollIfNeed(list: PagedList<ChatEntity>) {
        val currentChecked = lastChecked
        val firstUnread = list
            .indexOfFirst {
                lastChecked = it
                it.lastMessage != null &&
                        it.lastMessage?.status != MessageStatus.READ &&
                        it.lastMessage?.userId != viewModel.user.value?.id
            }
        val lm = binding.rvChats.layoutManager as LinearLayoutManager
        val first = lm.findFirstVisibleItemPosition()
        if (firstUnread != -1 && currentChecked?.id != lastChecked?.id) {
            if (first < 5) {
                binding.rvChats.scrollToPosition(firstUnread)
            }
        }
    }

    private fun showDeleteDialog(id: Long) {
        BlockUserDialog.create(
            requireContext(),
            getString(R.string.chats_delete),
            getString(R.string.chats_delete_description),
            getString(R.string.chats_delete_button),
            getString(R.string.chats_delete_cancel)
        ) { viewModel.onDelete(id) }.show()
    }

    private fun showMenu(v: View) {
        PopupMenu(view?.context, v, Gravity.TOP).apply {
            menuInflater.inflate(R.menu.create_new_chat_menu, this.menu)

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


    override fun onDestroyView() {
        rvChats.adapter = null
        super.onDestroyView()
    }


}
