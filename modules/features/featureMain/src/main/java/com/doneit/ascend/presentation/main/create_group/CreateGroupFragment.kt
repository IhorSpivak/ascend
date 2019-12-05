package com.doneit.ascend.presentation.main.create_group

import android.os.Bundle
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.base.BaseFragment
import com.doneit.ascend.presentation.main.common.ToolbarListener
import com.doneit.ascend.presentation.main.create_group.common.ParticipantAdapter
import com.doneit.ascend.presentation.main.databinding.FragmentCreateGroupBinding
import org.kodein.di.generic.instance

class CreateGroupFragment : BaseFragment<FragmentCreateGroupBinding>() {

    override val viewModelModule = CreateGroupViewModelModule.get(this)
    override val viewModel: CreateGroupContract.ViewModel by instance()

    private val adapter: ParticipantAdapter by lazy {
        ParticipantAdapter(mutableListOf())
    }

    override fun viewCreated(savedInstanceState: Bundle?) {
        binding.lifecycleOwner = this
        binding.model = viewModel
        binding.adapter = adapter
    }
}