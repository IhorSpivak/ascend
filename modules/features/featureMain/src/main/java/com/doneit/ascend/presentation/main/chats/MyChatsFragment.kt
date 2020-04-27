package com.doneit.ascend.presentation.main.chats

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import com.doneit.ascend.presentation.dialog.BlockUserDialog
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.base.BaseFragment
import com.doneit.ascend.presentation.main.chats.common.MyChatsAdapter
import com.doneit.ascend.presentation.main.common.gone
import com.doneit.ascend.presentation.main.databinding.FragmentMyChatsBinding
import com.doneit.ascend.presentation.utils.extensions.hideKeyboard
import com.doneit.ascend.presentation.utils.extensions.visible
import kotlinx.android.synthetic.main.fragment_my_chats.*
import org.kodein.di.generic.instance

class MyChatsFragment : BaseFragment<FragmentMyChatsBinding>() {
    override val viewModelModule = MyChatsViewModelModule.get(this)
    override val viewModel: MyChatsContract.ViewModel by instance()

    private var currentDialog: AlertDialog? = null

    private val adapter: MyChatsAdapter by lazy {
        MyChatsAdapter(
            {
                viewModel.onChatPressed(it)
            }, {
                showDeleteDialog(it)
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
        binding.clearSearch.setOnClickListener {
            tvSearch.text.clear()
            tvSearch.clearFocus()
            hideKeyboard()
            clearSearch.gone()
        }
        binding.tvSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                viewModel.filterTextAll.value = p0.toString()
                clearSearch.visible(p0.isNullOrEmpty().not())
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })
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
}
