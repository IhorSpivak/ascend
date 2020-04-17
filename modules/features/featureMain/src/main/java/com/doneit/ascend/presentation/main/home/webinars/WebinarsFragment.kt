package com.doneit.ascend.presentation.main.home.webinars

import android.os.Bundle
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.base.BaseFragment
import com.doneit.ascend.presentation.main.databinding.FragmentWebinarsBinding
import com.doneit.ascend.presentation.main.groups.group_list.common.GroupHorListAdapter
import com.doneit.ascend.presentation.utils.showDefaultError
import org.kodein.di.generic.instance

class WebinarsFragment : BaseFragment<FragmentWebinarsBinding>(){
    override val viewModelModule = WebinarsViewModelModule.get(this)
    override val viewModel: WebinarsContract.ViewModel by instance()

    private val adapter: GroupHorListAdapter by lazy {
        GroupHorListAdapter(null,
            {
                viewModel.onGroupClick(it)
            },
            {
                if (it.blocked != true) {
                    viewModel.onStartChatClick(it.id)
                } else {
                    showDefaultError(getString(R.string.error_group_user_removed))
                }
            }
        )
    }
    override fun viewCreated(savedInstanceState: Bundle?) {
    }

}