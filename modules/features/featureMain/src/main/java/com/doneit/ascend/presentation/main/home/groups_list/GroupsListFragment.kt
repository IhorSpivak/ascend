package com.doneit.ascend.presentation.main.home.groups_list

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.lifecycle.Observer
import com.doneit.ascend.domain.entity.group.GroupType
import com.doneit.ascend.domain.entity.user.UserEntity
import com.doneit.ascend.presentation.common.SideListDecorator
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.base.BaseActivity
import com.doneit.ascend.presentation.main.base.BaseFragment
import com.doneit.ascend.presentation.main.databinding.FragmentHomeGroupsBinding
import com.doneit.ascend.presentation.main.filter.FilterListener
import com.doneit.ascend.presentation.main.filter.FilterModel
import com.doneit.ascend.presentation.main.filter.FilterType
import com.doneit.ascend.presentation.main.filter.base_filter.BaseFilter
import com.doneit.ascend.presentation.main.groups.group_list.common.GroupHorListAdapter
import com.doneit.ascend.presentation.utils.convertCommunityToResId
import com.doneit.ascend.presentation.utils.showDefaultError
import org.kodein.di.generic.instance

class GroupsListFragment : BaseFragment<FragmentHomeGroupsBinding>(),
    FilterListener<FilterModel> {

    override val viewModel: GroupsListContract.ViewModel by instance()

    private val groupType: GroupType? by lazy {
        arguments?.getSerializable(GROUP_TYPE_KEY) as? GroupType
    }
    private val userId: Long? by lazy {
        arguments?.getLong(USER_ID_KEY)
    }

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

    @SuppressLint("DefaultLocale")
    private fun setButtonTitle(userEntity: UserEntity) {
        binding.tvFilter.text = when (groupType) {
            GroupType.WEBINAR,
            GroupType.SUPPORT,
            GroupType.MASTER_MIND -> getString(
                R.string.filter_groups_template,
                getString(convertCommunityToResId(userEntity.community.orEmpty(), groupType))
            )
            else -> getString(R.string.filter_groups)
        }
    }

    private fun observeData() {
        viewModel.groups.observe(viewLifecycleOwner, Observer {
            binding.hasGroups = it.groups.isNullOrEmpty().not()
            binding.networkStatus = (activity as BaseActivity).isNetworkAvailable
            adapter.setUser(it.user)
            adapter.submitList(it.groups)
            binding.srLayout.isRefreshing = false
        })
        viewModel.user.observe(viewLifecycleOwner, Observer {
            it ?: return@Observer
            setButtonTitle(it)
        })
    }

    private fun setListeners() {
        binding.srLayout.setOnRefreshListener {
            viewModel.updateData(userId)
        }
        binding.tvFilter.setOnClickListener {
            val filterType = when (groupType) {
                GroupType.SUPPORT -> FilterType.TAG
                GroupType.MASTER_MIND -> FilterType.GROUP_TYPE
                else -> FilterType.COMMUNITY
            }
            FilterFactory.create(viewModel.requestModel.value, filterType).show(
                childFragmentManager,
                BaseFilter::class.java.simpleName
            )
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.updateData(userId)
        binding.networkStatus = (activity as BaseActivity).isNetworkAvailable
    }

    override fun updateFilter(filter: FilterModel) {
        viewModel.requestModel.value?.let { groupsDTO ->
            viewModel.updateRequestModel(filter.toDTO(groupsDTO))
        }
    }

    companion object {

        private const val GROUP_TYPE_KEY = "key_group_type"
        private const val USER_ID_KEY = "key_user_id"

        fun newInstance(userId: Long? = null, groupType: GroupType? = null) =
            GroupsListFragment().apply {
                arguments = Bundle().apply {
                    groupType?.let {
                        putSerializable(GROUP_TYPE_KEY, it)
                        putLong(USER_ID_KEY, userId ?: return@let)
                    }
                }
            }
    }
}