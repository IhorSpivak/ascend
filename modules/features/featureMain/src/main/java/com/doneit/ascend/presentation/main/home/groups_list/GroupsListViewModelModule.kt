package com.doneit.ascend.presentation.main.home.groups_list

import androidx.lifecycle.ViewModelProvider
import com.doneit.ascend.domain.entity.group.GroupType
import com.vrgsoft.core.presentation.fragment.BaseFactory
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

object GroupsListViewModelModule {
    fun get(fragment: GroupsListFragment, groupType: GroupType?, userId: Long?) =
        Kodein.Module("GroupsList") {
            bind<ViewModelProvider.Factory>(tag = "GroupsList") with singleton {
                object : BaseFactory<GroupsListViewModel>() {
                    override fun createViewModel(): GroupsListViewModel = GroupsListViewModel(
                        instance(), instance(), instance(), userId, groupType
                    )
                }
            }
            bind<GroupsListContract.ViewModel>() with provider {
                fragment.vm<GroupsListViewModel>(instance(tag = "GroupsList"))
            }
        }
}