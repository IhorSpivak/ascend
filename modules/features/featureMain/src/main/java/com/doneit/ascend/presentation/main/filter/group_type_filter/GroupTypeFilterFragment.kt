package com.doneit.ascend.presentation.main.filter.group_type_filter

import android.os.Bundle
import com.doneit.ascend.domain.entity.group.GroupType
import com.doneit.ascend.presentation.main.common.visible
import com.doneit.ascend.presentation.main.filter.community_filter.CommunityFilter
import org.kodein.di.Kodein
import org.kodein.di.generic.instance

class GroupTypeFilterFragment : CommunityFilter<GroupTypeFilterModel>() {

    override val viewModelModule: Kodein.Module
        get() = GroupTypeFilterViewModelModule.get(this)

    override val viewModel: GroupTypeFilterContract.ViewModel by instance()

    override fun setupBinding() {
        super.setupBinding()
        with(binding) {
            rgParticipants.visible()
            tvParticipantsNumber.visible()
            rgParticipants.setOnCheckedChangeListener { _, checkedId ->
                if (checkedId == radioPublic.id) viewModel.setGroupType(GroupType.MASTER_MIND)
                else viewModel.setGroupType(GroupType.INDIVIDUAL)
            }
        }
    }

    override fun initFilter(initFilter: GroupTypeFilterModel) {
        super.initFilter(initFilter)
        when(initFilter.groupType){
            GroupType.INDIVIDUAL -> binding.radioPrivate.isChecked = true
            else -> binding.radioPublic.isChecked = true
        }
    }

    companion object {
        fun newInstance(filter: GroupTypeFilterModel) = GroupTypeFilterFragment().apply {
            arguments = Bundle().apply {
                putParcelable(KEY_INIT_FILTER, filter)
            }
        }
    }

}