package com.doneit.ascend.presentation.profile.edit_phone.verify_phone

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.base.BaseFragment
import com.doneit.ascend.presentation.main.base.CommonViewModelFactory
import com.doneit.ascend.presentation.main.databinding.FragmentChangePhoneVerifyBinding
import com.doneit.ascend.presentation.models.PresentationMessage
import com.doneit.ascend.presentation.profile.edit_phone.EditPhoneViewModel
import com.doneit.ascend.presentation.utils.Messages
import com.doneit.ascend.presentation.utils.extensions.hideKeyboard
import com.doneit.ascend.presentation.utils.extensions.vmShared
import com.doneit.ascend.presentation.utils.showInfoDialog
import com.doneit.ascend.presentation.views.SmsCodeView
import kotlinx.android.synthetic.main.fragment_change_phone_verify.*
import org.kodein.di.Kodein
import org.kodein.di.direct
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton


class VerifyChangePhoneFragment : BaseFragment<FragmentChangePhoneVerifyBinding>() {

    override val viewModelModule = Kodein.Module(this::class.java.simpleName) {
        //todo resolve extra factory provide
        bind<ViewModelProvider.Factory>(tag = EditPhoneViewModel::class.java.simpleName) with singleton {
            CommonViewModelFactory(
                kodein.direct
            )
        }
        //di should contains corresponding ViewModel from EditPhoneFragment' module for now
        bind<VerifyChangePhoneContract.ViewModel>() with provider { vmShared<EditPhoneViewModel>(instance(tag = EditPhoneViewModel::class.java.simpleName)) }
    }

    override val viewModel: VerifyChangePhoneContract.ViewModel by instance()

    override fun viewCreated(savedInstanceState: Bundle?) {
        binding.model = viewModel

        binding.toolbar.imBack.setOnClickListener {
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

    override fun handleSuccessMessage(message: PresentationMessage) {
        when (message.id) {
            Messages.PASSWORD_SENT.getId() -> {
                showInfoDialog(
                    getString(R.string.title_password_sent),
                    getString(R.string.content_password_sent)
                )
            }
        }
    }
}