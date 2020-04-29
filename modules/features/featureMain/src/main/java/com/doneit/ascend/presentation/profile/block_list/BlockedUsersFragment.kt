package com.doneit.ascend.presentation.profile.block_list

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.lifecycle.Observer
import com.doneit.ascend.domain.entity.chats.BlockedUserEntity
import com.doneit.ascend.presentation.dialog.BlockUserDialog
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.base.BaseFragment
import com.doneit.ascend.presentation.main.common.gone
import com.doneit.ascend.presentation.main.databinding.FragmentBlockedUsersBinding
import com.doneit.ascend.presentation.profile.block_list.common.BlockedUsersAdapter
import com.doneit.ascend.presentation.utils.extensions.visible
import kotlinx.android.synthetic.main.fragment_blocked_users.clearSearch
import kotlinx.android.synthetic.main.fragment_blocked_users.tvSearch
import kotlinx.android.synthetic.main.fragment_my_chats.*
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
            emptyList.visible(it.isNullOrEmpty())
            blockedUsersAdapter.submitList(it)
        })

        binding.tvSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                viewModel.filterTextAll.value = p0.toString()
                emptyList.setText(if (p0.isNullOrEmpty()) R.string.no_blocked_users else R.string.no_results)
                clearSearch.visible(p0.isNullOrEmpty().not())
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })

        binding.clearSearch.setOnClickListener {
            tvSearch.text.clear()
            clearSearch.gone()
        }
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