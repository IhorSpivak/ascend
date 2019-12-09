package com.doneit.ascend.presentation.profile

import android.os.Bundle
import com.doneit.ascend.presentation.main.base.BaseFragment
import com.doneit.ascend.presentation.main.databinding.FragmentProfileBinding
import org.kodein.di.generic.instance


class ProfileFragment : BaseFragment<FragmentProfileBinding>() {

    override val viewModelModule = ProfileViewModelModule.get(this)
    override val viewModel: ProfileContract.ViewModel by instance()

    override fun viewCreated(savedInstanceState: Bundle?) {
        binding.model = viewModel
    }
}
