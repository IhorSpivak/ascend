package com.doneit.ascend.presentation.main.master_mind_info.mm_content.livestreams

import com.doneit.ascend.presentation.main.home.webinars.WebinarsContract


import android.os.Bundle
import android.widget.RadioButton
import androidx.core.view.children
import androidx.core.view.get
import androidx.lifecycle.Observer
import com.doneit.ascend.domain.entity.user.UserEntity
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.base.BaseFragment
import com.doneit.ascend.presentation.main.databinding.FragmentFilterMmLivestreamsBinding
import com.doneit.ascend.presentation.main.databinding.FragmentMmLiveStreamsBinding
import com.doneit.ascend.presentation.main.databinding.FragmentWebinarsBinding
import com.doneit.ascend.presentation.main.groups.group_list.common.GroupHorListAdapter
import com.doneit.ascend.presentation.main.home.webinars.common.WebinarFilter
import com.doneit.ascend.presentation.main.master_mind_info.mm_content.posts.MMPostsFragment
import com.doneit.ascend.presentation.utils.showDefaultError
import org.kodein.di.generic.instance

class MMLiveStreamsFragment : BaseFragment<FragmentMmLiveStreamsBinding>(){

    override val viewModelModule = MMLiveStreamsViewModelModule.get(this)
    override val viewModel: MMLiveStreamsContract.ViewModel by instance()


    private val userId: Long by lazy {
        requireArguments().getLong(KEY_MM_ID)
    }

    private val user by lazy {
        requireArguments().getParcelable<UserEntity>(KEY_USER)
    }


    private val adapter: GroupHorListAdapter by lazy {
        GroupHorListAdapter(
            null,
            {
                viewModel.onGroupClick(it)
            },
            {
                if (it.blocked != true) {
                    viewModel.onStartChatClick(it.id, it.groupType!!)
                } else {
                    showDefaultError(getString(R.string.error_group_user_removed))
                }
            },
            null
        )
    }
    override fun viewCreated(savedInstanceState: Bundle?) {
        binding.apply {
            model = viewModel
        }

        viewModel.updateFilter(WebinarFilter.FAMILY)
        viewModel.checkUser(user!!)


        viewModel.groups.observe(viewLifecycleOwner, Observer {
            adapter.setUser(user!!)

            adapter.submitList(it.groups)
        })

    }


    companion object {
        private const val KEY_MM_ID = "ID"
        private const val KEY_USER = "USER"
        fun newInstance(userId: Long,userEntity: UserEntity) = MMLiveStreamsFragment()
            .apply {
                arguments = Bundle().apply {
                    putLong(KEY_MM_ID, userId)
                    putParcelable(KEY_USER, userEntity)

                }
            }
    }
}