package com.doneit.ascend.presentation.main.chats.chat_members

import android.os.Bundle
import androidx.lifecycle.Observer
import com.doneit.ascend.domain.entity.chats.MemberEntity
import com.doneit.ascend.domain.entity.dto.ChatType
import com.doneit.ascend.domain.entity.user.UserEntity
import com.doneit.ascend.presentation.main.base.BaseFragment
import com.doneit.ascend.presentation.main.chats.new_chat.common.ChatMemberAdapter
import com.doneit.ascend.presentation.main.databinding.FragmentChatMembersBinding
import kotlinx.android.synthetic.main.fragment_chat_members.*
import org.kodein.di.Kodein
import org.kodein.di.generic.instance

class ChatMembersFragment : BaseFragment<FragmentChatMembersBinding>() {
    override val viewModel: ChatMembersContract.ViewModel by instance()

    override val viewModelModule: Kodein.Module
        get() = ChatMembersViewModelModule.get(this)

    private val chatMembersAdapter: ChatMemberAdapter by lazy {
        ChatMemberAdapter(
            viewModel::removeMember,
            viewModel::blockMember
        )
    }

    override fun viewCreated(savedInstanceState: Bundle?) {
        checkArguments()
        initView()
        observeEvents()
    }

    private fun initView() {
        binding.apply {
            model = viewModel
            chatType = requireArguments().getSerializable(KEY_CHAT_TYPE) as ChatType
            rvMembers.adapter = chatMembersAdapter
        }
    }


    //This cast is checked because of newInstance constructor
    @Suppress("UNCHECKED_CAST")
    private fun checkArguments() {
        val membersArray = requireArguments()
            .getParcelableArray(KEY_MEMBERS_LIST)
            .orEmpty() as Array<MemberEntity>
        val chatId = requireArguments().getLong(KEY_CHAT_ID)
        val user = requireArguments()
            .getParcelable<UserEntity>(KEY_USER)!!
        viewModel.setUser(user)
        viewModel.setMembers(chatId, membersArray.filter { it.id != user.id }.toMutableList())
    }

    private fun observeEvents() {
        viewModel.members.observe(this, Observer {
            it ?: return@Observer
            chatMembersAdapter.submitList(it)
        })
        viewModel.query.observe(this, Observer {
            chatMembersAdapter.filter.filter(it)
        })
        viewModel.user.observe(this, Observer {
            it ?: return@Observer
            chatMembersAdapter.isUserOwner = it.id == requireArguments().getLong(KEY_CHAT_OWNER)
        })
    }

    override fun onDestroyView() {
        rvMembers.adapter = null
        super.onDestroyView()
    }

    companion object {

        private const val KEY_CHAT_ID = "CHAT_ID"
        private const val KEY_MEMBERS_LIST = "MEMBERS_LIST"
        private const val KEY_USER = "USER"
        private const val KEY_CHAT_OWNER = "CHAT_OWNER"
        private const val KEY_CHAT_TYPE = "CHAT_TYPE"

        fun newInstance(
            chatId: Long,
            chatOwnerId: Long,
            chatType: ChatType,
            members: List<MemberEntity>,
            user: UserEntity
        ): ChatMembersFragment {
            return ChatMembersFragment().apply {
                arguments = Bundle().apply {
                    putLong(KEY_CHAT_ID, chatId)
                    putLong(KEY_CHAT_OWNER, chatOwnerId)
                    putParcelable(KEY_USER, user)
                    putSerializable(KEY_CHAT_TYPE, chatType)
                    putParcelableArray(KEY_MEMBERS_LIST, members.toTypedArray())
                }
            }
        }
    }
}