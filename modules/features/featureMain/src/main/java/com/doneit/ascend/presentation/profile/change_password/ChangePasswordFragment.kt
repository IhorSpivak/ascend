package com.doneit.ascend.presentation.profile.change_password

import android.os.Bundle
import com.doneit.ascend.presentation.main.base.BaseFragment
import com.doneit.ascend.presentation.main.databinding.ChangePasswordFragmentBinding
import org.kodein.di.generic.instance

class ChangePasswordFragment : BaseFragment<ChangePasswordFragmentBinding>() {

    override val viewModelModule = ChangePasswordViewModelModule.get(this)
    override val viewModel: ChangePasswordContract.ViewModel by instance()

    override fun viewCreated(savedInstanceState: Bundle?) {
        binding.model = viewModel
    }
}
