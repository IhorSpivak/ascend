package com.doneit.ascend.presentation.main.groups

import android.os.Bundle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.doneit.ascend.presentation.main.MainActivity
import com.doneit.ascend.presentation.main.base.CommonViewModelFactory
import com.doneit.ascend.presentation.main.base.argumented.ArgumentedFragment
import com.doneit.ascend.presentation.main.databinding.FragmentGroupsBinding
import com.doneit.ascend.presentation.main.extensions.vmShared
import com.doneit.ascend.presentation.main.groups.common.GroupAdapter
import com.doneit.ascend.presentation.main.groups.common.GroupsArgs
import com.doneit.ascend.presentation.main.home.HomeViewModel
import org.kodein.di.Kodein
import org.kodein.di.direct
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

class GroupsFragment : ArgumentedFragment<FragmentGroupsBinding, GroupsArgs>() {

    override val viewModelModule = Kodein.Module(this::class.java.simpleName){
        //di should contains corresponding ViewModel from HomeFragment' module for now
        bind<GroupsContract.ViewModel>() with provider { vmShared<HomeViewModel>(instance(tag = MainActivity.HOME_VM_TAG)) }
    }

    override val viewModel: GroupsContract.ViewModel by instance()

    private val adapter: GroupAdapter by lazy {
        GroupAdapter(mutableListOf())
    }

    override fun viewCreated(savedInstanceState: Bundle?) {
        binding.lifecycleOwner = this
        binding.model = viewModel
        binding.adapter = adapter
    }

    override fun onResume() {
        super.onResume()
        viewModel.updateGroups()
    }
}