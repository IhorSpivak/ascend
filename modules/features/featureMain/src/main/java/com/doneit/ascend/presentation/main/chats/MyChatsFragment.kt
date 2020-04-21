package com.doneit.ascend.presentation.main.chats

import android.os.Bundle
import androidx.lifecycle.Observer
import com.doneit.ascend.presentation.main.base.BaseFragment
import com.doneit.ascend.presentation.main.chats.common.MyChatsAdapter
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
                viewModel.onDelete(it)
            }
        )
    }

    override fun viewCreated(savedInstanceState: Bundle?) {
        binding.apply {
            model = viewModel
            btnBack.setOnClickListener {
                viewModel.onBackPressed()
            }
            rvChats.adapter = adapter
            tvNewChat.setOnClickListener {
                viewModel.onNewChatPressed()
            }
        }

        viewModel.chats.observe(viewLifecycleOwner, Observer {
            emptyList.visible(it.isNullOrEmpty())
            adapter.submitList(it)
        })
    }
}
