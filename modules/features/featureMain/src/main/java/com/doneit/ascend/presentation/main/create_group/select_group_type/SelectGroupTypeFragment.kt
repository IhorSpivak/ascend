package com.doneit.ascend.presentation.main.create_group.select_group_type

import android.os.Bundle
import com.doneit.ascend.presentation.main.base.BaseFragment
import com.doneit.ascend.presentation.main.databinding.FragmentSelectGroupTypeBinding
import org.kodein.di.generic.instance

class SelectGroupTypeFragment: BaseFragment<FragmentSelectGroupTypeBinding>() {

    override val viewModelModule = SelectGroupTypeViewModelModule.get(this)
    override val viewModel: SelectGroupTypeContract.ViewModel by instance()

    override fun viewCreated(savedInstanceState: Bundle?) {
        binding.model = viewModel
    }
}