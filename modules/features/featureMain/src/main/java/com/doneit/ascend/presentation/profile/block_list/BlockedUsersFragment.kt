package com.doneit.ascend.presentation.profile.block_list

import android.os.Bundle
import androidx.lifecycle.Observer
import com.doneit.ascend.domain.entity.chats.BlockedUserEntity
import com.doneit.ascend.presentation.dialog.BlockUserDialog
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.base.BaseFragment
import com.doneit.ascend.presentation.profile.block_list.common.BlockedUsersAdapter
import com.doneit.ascend.presentation.main.databinding.FragmentBlockedUsersBinding
import org.kodein.di.generic.instance

class BlockedUsersFragment: BaseFragment<FragmentBlockedUsersBinding>() {
    override val viewModelModule =
        BlockedUsersViewModelModule.get(
            this
        )
    override val viewModel: BlockedUsersContract.ViewModel by instance()

    private val blockedUsersAdapter: BlockedUsersAdapter by lazy {
        BlockedUsersAdapter {
            onUnblockClick(it)
        }
    }
    override fun viewCreated(savedInstanceState: Bundle?) {
        binding.apply {
            recyclerViewBlockedUsers.adapter = blockedUsersAdapter
            btnBack.setOnClickListener {
                viewModel.onBackPressed()
            }
        }
        viewModel.blockedUsers.observe(this, Observer {
            blockedUsersAdapter.submitList(it)
        })
    }
    private fun onUnblockClick(user: BlockedUserEntity){
        context?.let {
            BlockUserDialog.create(
                it,
                getString(R.string.chats_mm_unblock),
                getString(R.string.chats_mm_unblock_description),
                getString(R.string.chats_mm_unblock_button),
                getString(R.string.chats_mm_unblock_cancel)
            ){viewModel.onUnblockUser(user)}.show()
        }
    }
}