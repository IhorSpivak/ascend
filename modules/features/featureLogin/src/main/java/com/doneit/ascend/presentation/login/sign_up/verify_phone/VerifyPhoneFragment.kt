package com.doneit.ascend.presentation.login.sign_up.verify_phone

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.doneit.ascend.presentation.login.LogInActivity
import com.doneit.ascend.presentation.login.R
import com.doneit.ascend.presentation.login.databinding.FragmentVerifyPhoneBinding
import com.doneit.ascend.presentation.login.sign_up.SignUpViewModel
import com.doneit.ascend.presentation.login.views.SmsCodeView
import com.doneit.ascend.presentation.main.base.BaseFragment
import com.doneit.ascend.presentation.main.base.CommonViewModelFactory
import com.doneit.ascend.presentation.main.extensions.hideKeyboard
import com.doneit.ascend.presentation.main.extensions.vmShared
import com.doneit.ascend.presentation.main.models.PresentationMessage
import com.doneit.ascend.presentation.utils.Messages
import com.doneit.ascend.presentation.utils.showDefaultError
import com.doneit.ascend.presentation.utils.showInfoDialog
import kotlinx.android.synthetic.main.fragment_verify_phone.*
import kotlinx.android.synthetic.main.toolbar.*
import org.kodein.di.Kodein
import org.kodein.di.direct
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

class VerifyPhoneFragment : BaseFragment<FragmentVerifyPhoneBinding>() {

    override val viewModelModule = Kodein.Module(this::class.java.simpleName){
        bind<ViewModelProvider.Factory>() with singleton { CommonViewModelFactory(kodein.direct) }
        //di should contains corresponding ViewModel from SignUpFragments' module for now
        bind<VerifyPhoneContract.ViewModel>() with provider { vmShared<SignUpViewModel>(instance(tag = LogInActivity.SIGN_UP_VM_TAG)) }
    }

    override val viewModel: VerifyPhoneContract.ViewModel by instance()

    override fun viewCreated(savedInstanceState: Bundle?) {
        binding.model = viewModel

        imBack.setOnClickListener {
            viewModel.onBackClick(false)
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

    override fun handleSuccessMessage(message: PresentationMessage) {
        when(message.id) {
            Messages.PASSWORD_SENT.getId() -> {
                showInfoDialog(
                    getString(R.string.title_password_sent),
                    getString(R.string.content_password_sent)
                )
            }
        }
    }
}