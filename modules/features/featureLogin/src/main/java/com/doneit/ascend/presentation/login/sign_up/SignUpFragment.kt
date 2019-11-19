package com.doneit.ascend.presentation.login.sign_up

import android.os.Bundle
import com.doneit.ascend.presentation.login.databinding.SignUpFragmentBinding
import com.vrgsoft.core.presentation.fragment.BaseFragment
import com.vrgsoft.core.presentation.fragment.BaseViewModel
import org.kodein.di.generic.instance

class SignUpFragment : BaseFragment<SignUpFragmentBinding>() {

    override val viewModelModule = SignUpViewModelModule.get(this)
    override val viewModel: SignUpContract.ViewModel by instance()

    override fun viewCreated(savedInstanceState: Bundle?) {
        binding.model = viewModel
    }
}
