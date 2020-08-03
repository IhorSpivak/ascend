package com.doneit.ascend.presentation.main.home.community_feed.channels.create_channel.add_members

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.doneit.ascend.domain.entity.AttendeeEntity
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.base.BaseFragment
import com.doneit.ascend.presentation.main.common.gone
import com.doneit.ascend.presentation.main.databinding.FragmentChannelAddMembersBinding
import com.doneit.ascend.presentation.main.databinding.ItemChipViewBinding
import com.doneit.ascend.presentation.main.home.community_feed.channels.create_channel.CreateChannelContract
import com.doneit.ascend.presentation.main.home.community_feed.channels.create_channel.add_members.common.ChatMembersAdapter
import com.doneit.ascend.presentation.utils.GlideChip
import com.doneit.ascend.presentation.utils.extensions.hideKeyboard
import com.doneit.ascend.presentation.utils.extensions.showKeyboard
import com.doneit.ascend.presentation.utils.extensions.visible
import com.google.android.material.chip.ChipGroup
import kotlinx.android.synthetic.main.fragment_my_chats.*
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider


class AddMembersFragment : BaseFragment<FragmentChannelAddMembersBinding>() {

    override val viewModelModule = Kodein.Module(this::class.java.simpleName) {
        bind<AddMembersContract.ViewModel>() with provider {
            instance<CreateChannelContract.ViewModel>()
        }
    }
    override val viewModel: AddMembersContract.ViewModel by instance()

    private val memberAdapter: ChatMembersAdapter by lazy {
        ChatMembersAdapter(
            { viewModel.onAddMember(it) },
            { viewModel.onRemoveMember(it) },
            viewModel
        )
    }

    override fun viewCreated(savedInstanceState: Bundle?) {
        connectionObserver.networkStateChanged.observe(viewLifecycleOwner, Observer {
            //viewModel.connectionAvailable.postValue(it)
        })
        binding.apply {
            root.apply {
                isFocusableInTouchMode = true
                requestFocus()
                setOnKeyListener { view, i, keyEvent ->
                    if (i == KeyEvent.KEYCODE_BACK) {
                        viewModel.onLocalBackPressed()
                        true
                    }
                    false
                }
            }
            model = viewModel
            buttonComplete.setOnClickListener {
                viewModel.complete()
            }
        }
        viewModel.searchResult.observe(this, Observer {
            emptyList.visible(it.isEmpty() && tvSearch.text.length > 1)
            memberAdapter.submitList(it)
            binding.searchVis = it.isNotEmpty()
        })
        viewModel.addedMembers.observe(this, Observer {
            addChipToViewGroup(binding.chipContainer, it)
        })
        binding.apply {
            //viewModel.onQueryTextChange(tvSearch.text.toString())
            root.apply {
                isFocusableInTouchMode = true
                requestFocus()
                setOnKeyListener { view, i, keyEvent ->
                    if (i == KeyEvent.KEYCODE_BACK) {
                        viewModel.onLocalBackPressed()
                        true
                    }
                    false
                }
            }
            searchVis = false
            rvMembers.adapter = memberAdapter
            tvSearch.setOnEditorActionListener(object : TextView.OnEditorActionListener {
                override fun onEditorAction(p0: TextView?, p1: Int, p2: KeyEvent?): Boolean {
                    if (p1 == EditorInfo.IME_ACTION_SEARCH) {
                        if (tvSearch.text.length > 1) {
                            hideKeyboard()
                        }
                        return true
                    }
                    return false
                }
            })
            clearSearch.setOnClickListener {
                tvSearch.text.clear()
                tvSearch.clearFocus()
                emptyList.gone()
                searchVis = false
            }
            binding.tvSearch.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(p0: Editable?) {
                    viewModel.onQueryTextChange(p0.toString())
                }

                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    //empty
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    //empty
                }
            })
        }
        viewModel.searchResult.observe(this, Observer {
            emptyList.visible(it.isEmpty() && tvSearch.text.length > 1)
            memberAdapter.submitList(it)
            binding.searchVis = it.isNotEmpty()
        })
        binding.tvSearch.showKeyboard()
    }

    private fun addChipToViewGroup(chipGroup: ChipGroup, members: List<AttendeeEntity>) {
        //todo refactor:
        chipGroup.removeAllViews()
        members.forEach { attendee ->
            val binding: ItemChipViewBinding = DataBindingUtil.inflate(
                LayoutInflater.from(requireContext()),
                R.layout.item_chip_view,
                chipGroup,
                false
            )

            binding.user = attendee
            val chip = binding.root as GlideChip

            chip.setIconUrl(attendee.imageUrl, attendee.fullName)
            chip.setOnCloseIconClickListener {
                viewModel.onDeleteMember(attendee.id)
            }
            chipGroup.addView(chip)
        }
    }

    fun getContainerId() = R.id.container
}