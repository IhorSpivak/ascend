package com.doneit.ascend.presentation.main.home.group

import android.os.Bundle
import android.view.View
import com.doneit.ascend.presentation.main.MainActivity
import com.doneit.ascend.presentation.main.base.argumented.ArgumentedFragment
import com.doneit.ascend.presentation.main.databinding.FragmentGroupsBinding
import com.doneit.ascend.presentation.main.extensions.vmShared
import com.doneit.ascend.presentation.main.home.group.common.GroupAdapter
import com.doneit.ascend.presentation.main.home.group.common.GroupsArgs
import com.doneit.ascend.presentation.main.home.HomeViewModel
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider

class GroupsFragment : ArgumentedFragment<FragmentGroupsBinding, GroupsArgs>() {

    override val viewModelModule = Kodein.Module(this::class.java.simpleName){
        //di should contains corresponding ViewModel from HomeFragment' module for now
        bind<GroupsContract.ViewModel>() with provider { vmShared<HomeViewModel>(instance(tag = MainActivity.HOME_VM_TAG)) }
    }

    override val viewModel: GroupsContract.ViewModel by instance()

    private val adapter: GroupAdapter by lazy {
        GroupAdapter(mutableListOf())
    }

    private lateinit var groupsArg: GroupsArgs

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        groupsArg = arguments!!.getParcelable(KEY_ARGS)!!
    }

    override fun viewCreated(savedInstanceState: Bundle?) {
        binding.lifecycleOwner = this
        binding.model = viewModel
        binding.adapter = adapter
    }

    override fun onResume() {
        super.onResume()
        viewModel.applyArguments(groupsArg)
        viewModel.updateGroups()
    }
}