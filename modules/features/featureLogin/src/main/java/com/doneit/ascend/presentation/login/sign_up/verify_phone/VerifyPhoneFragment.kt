package com.doneit.ascend.presentation.login.sign_up.verify_phone

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.doneit.ascend.presentation.login.databinding.FragmentVerifyPhoneBinding
import com.doneit.ascend.presentation.login.models.PresentationSignUpModel
import com.doneit.ascend.presentation.login.sign_up.SignUpViewModel
import com.doneit.ascend.presentation.main.base.CommonViewModelFactory
import com.doneit.ascend.presentation.main.extensions.vmShared
import com.vrgsoft.core.presentation.fragment.BaseFragment
import kotlinx.android.synthetic.main.toolbar.*
import org.kodein.di.Kodein
import org.kodein.di.direct
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton

class VerifyPhoneFragment : BaseFragment<FragmentVerifyPhoneBinding>() {

    override val viewModelModule = Kodein.Module(this::class.java.simpleName){
        bind<ViewModelProvider.Factory>() with singleton { CommonViewModelFactory(kodein.direct) }
        //di should contains for now corresponding ViewModel from SignUpFragments' module
        bind<VerifyPhoneContract.ViewModel>() with singleton { vmShared<SignUpViewModel>(instance()) }
    }

    override val viewModel: VerifyPhoneContract.ViewModel by instance()

    override fun viewCreated(savedInstanceState: Bundle?) {
        binding.model = viewModel

        imBack.setOnClickListener {
            viewModel.onBackClick()
        }
    }
}