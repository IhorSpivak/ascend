package com.doneit.ascend.presentation.main.home.master_mind

import android.os.Bundle
import androidx.lifecycle.Observer
import com.doneit.ascend.domain.entity.dto.GroupListDTO
import com.doneit.ascend.presentation.common.SideListDecorator
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.base.BaseActivity
import com.doneit.ascend.presentation.main.base.BaseFragment
import com.doneit.ascend.presentation.main.databinding.FragmentHomeGroupsBinding
import com.doneit.ascend.presentation.main.filter.FilterListener
import com.doneit.ascend.presentation.main.filter.base_filter.BaseFilter
import com.doneit.ascend.presentation.main.filter.community_filter.CommunityFilterModel
import com.doneit.ascend.presentation.main.filter.tag_filter.TagFilterFragment
import com.doneit.ascend.presentation.main.filter.tag_filter.TagFilterModel
import com.doneit.ascend.presentation.main.groups.group_list.common.GroupHorListAdapter
import com.doneit.ascend.presentation.utils.showDefaultError
import org.kodein.di.generic.instance

class MasterMindFragment : BaseFragment<FragmentHomeGroupsBinding>(), FilterListener<CommunityFilterModel> {

    override val viewModel: MasterMindContract.ViewModel by instance()

    private val adapter: GroupHorListAdapter by lazy {
        GroupHorListAdapter(
            null,
            {
                viewModel.onGroupClick(it.id)
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
        setupBinding()
        observeData()
        setListeners()
    }

    private fun setupBinding() = with(binding) {
        model = viewModel
        hasGroups = true
        networkStatus = (activity as BaseActivity).isNetworkAvailable
        rvGroups.addItemDecoration(
            SideListDecorator(
                paddingTop = resources.getDimension(R.dimen.search_list_top_padding).toInt()
            )
        )
        rvGroups.adapter = adapter
    }

    private fun observeData() {
        viewModel.groups.observe(viewLifecycleOwner, Observer {
            binding.hasGroups = it.groups.isNullOrEmpty().not()
            binding.networkStatus = (activity as BaseActivity).isNetworkAvailable
            adapter.setUser(it.user)
            adapter.submitList(it.groups)
            binding.srLayout.isRefreshing = false
        })
    }

    private fun setListeners() {
        binding.srLayout.setOnRefreshListener {
            viewModel.updateData()
        }
        binding.tvFilter.setOnClickListener {
            TagFilterFragment.newInstance(TagFilterModel()).show(
                childFragmentManager,
                BaseFilter::class.java.simpleName
            )
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.updateData()
        binding.networkStatus = (activity as BaseActivity).isNetworkAvailable
    }

    override fun updateFilter(filter: CommunityFilterModel) {
        viewModel.requestModel.value?.let { groupsDTO ->
            viewModel.updateRequestModel(
                GroupListDTO(
                    perPage = groupsDTO.perPage,
                    sortColumn = groupsDTO.sortColumn,
                    sortType = groupsDTO.sortType,
                    groupType = groupsDTO.groupType,
                    groupStatus = groupsDTO.groupStatus,
                    daysOfWeen = filter.selectedDays.map { it.ordinal },
                    timeFrom = filter.timeFrom,
                    timeTo = filter.timeTo,
                    community = filter.community?.title
                )
            )
        }
    }
}