package com.doneit.ascend.presentation.main.home.groups

import android.os.Bundle
import android.view.LayoutInflater
import androidx.core.view.children
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.doneit.ascend.domain.entity.TagEntity
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.base.BaseFragment
import com.doneit.ascend.presentation.main.databinding.FragmentTabGroupsBinding
import com.doneit.ascend.presentation.main.databinding.TagChipViewBinding
import com.doneit.ascend.presentation.main.groups.group_list.common.GroupHorListAdapter
import com.doneit.ascend.presentation.utils.showDefaultError
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import org.kodein.di.generic.instance

class GroupsFragment : BaseFragment<FragmentTabGroupsBinding>() {
    override val viewModelModule = GroupsViewModelModule.get(this)
    override val viewModel: GroupsContract.ViewModel by instance()

    private val adapter: GroupHorListAdapter by lazy {
        GroupHorListAdapter(
            null,
            {
                viewModel.onGroupClick(it)
            },
            {
                if (it.blocked != true) {
                    viewModel.onStartChatClick(it.id)
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
            rvGroups.adapter = adapter

            radioGroup.setOnCheckedChangeListener { radioGroup, i ->
                val view = radioGroup.children.firstOrNull { it.id == i }
                view?.let { viewModel.updateFilter(it.tag as TagEntity) }
                    ?: viewModel.updateFilter()
            }
        }



        viewModel.isRefreshing.observe(viewLifecycleOwner, Observer {
            binding.swipeRefresh.isRefreshing = it
        })
        binding.swipeRefresh.setOnRefreshListener {
            binding.radioGroup.children.firstOrNull { (it as Chip).isChecked }.run {
                this?.let { viewModel.updateFilter(it.tag as TagEntity?) }
                    ?: viewModel.updateFilter()
            }
        }

        viewModel.groups.observe(viewLifecycleOwner, Observer {
            adapter.setUser(it.user)
            adapter.submitList(it.groups)
        })

        viewModel.userLiveData.observe(viewLifecycleOwner, Observer { user ->
            viewModel.checkUser(user)
        })

        viewModel.tags.observe(viewLifecycleOwner, Observer {
            addChipToViewGroup(binding.radioGroup, it)
        })
    }

    override fun onResume() {
        super.onResume()
        viewModel.updateFilter()
    }

    private fun addChipToViewGroup(chipGroup: ChipGroup, tags: List<TagEntity>) {
        chipGroup.removeAllViews()
        tags.forEach { tag ->
            val binding: TagChipViewBinding = DataBindingUtil.inflate(
                LayoutInflater.from(requireContext()),
                R.layout.tag_chip_view,
                chipGroup,
                false
            )
            binding.tag = tag
            val chip = binding.root
            chip.tag = tag
            chipGroup.addView(chip)
        }
    }

}