package com.doneit.ascend.presentation.login.sign_up.verify_phone

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.doneit.ascend.presentation.login.databinding.FragmentVerifyPhoneBinding
import com.doneit.ascend.presentation.login.sign_up.SignUpViewModel
import com.doneit.ascend.presentation.login.views.SmsCodeView
import com.doneit.ascend.presentation.main.base.CommonViewModelFactory
import com.doneit.ascend.presentation.main.extensions.hideKeyboard
import com.doneit.ascend.presentation.main.extensions.vmShared
import com.vrgsoft.core.presentation.fragment.BaseFragment
import kotlinx.android.synthetic.main.fragment_verify_phone.*
import kotlinx.android.synthetic.main.toolbar.*
import org.kodein.di.Kodein
import org.kodein.di.direct
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton

class VerifyPhoneFragment : BaseFragment<FragmentVerifyPhoneBinding>() {

    override val viewModelModule = Kodein.Module(this::class.java.simpleName){
        bind<ViewModelProvider.Factory>() with singleton { CommonViewModelFactory(kodein.direct) }
        //di should contains corresponding ViewModel from SignUpFragments' module for now
        bind<VerifyPhoneContract.ViewModel>() with singleton { vmShared<SignUpViewModel>(instance()) }
    }

    override val viewModel: VerifyPhoneContract.ViewModel by instance()

    override fun viewCreated(savedInstanceState: Bundle?) {
        binding.model = viewModel

        imBack.setOnClickListener {
            viewModel.onBackClick()
        }

        smsCode.setSubmitListener(object : SmsCodeView.OnSubmitListener {
            override fun onSubmit() {
                hideKeyboard()
                viewModel.onVerifyClick()
            }
        })
        smsCode.requestFirstFocus()

        viewModel.sendCode()
    }
}