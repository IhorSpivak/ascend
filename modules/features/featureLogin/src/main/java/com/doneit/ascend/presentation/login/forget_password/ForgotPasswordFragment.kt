package com.doneit.ascend.presentation.login.forget_password

import android.os.Bundle
import androidx.lifecycle.Observer
import com.doneit.ascend.presentation.login.databinding.FragmentForgotPasswordBinding
import com.doneit.ascend.presentation.login.utils.getNotNull
import com.doneit.ascend.presentation.main.base.BaseFragment
import com.doneit.ascend.presentation.main.extensions.hideKeyboard
import com.doneit.ascend.presentation.main.models.PresentationMessage
import com.doneit.ascend.presentation.utils.Messages
import com.doneit.ascend.presentation.utils.showDefaultError
import kotlinx.android.synthetic.main.group_phone.*
import kotlinx.android.synthetic.main.toolbar.*
import org.kodein.di.generic.instance

class ForgotPasswordFragment : BaseFragment<FragmentForgotPasswordBinding>() {

    override val viewModelModule = ForgotPasswordViewModelModule.get(this)
    override val viewModel: ForgotPasswordContract.ViewModel by instance()

    override fun viewCreated(savedInstanceState: Bundle?) {
        binding.lifecycleOwner = this
        binding.model = viewModel

        imBack.setOnClickListener {
            viewModel.onBackClick()
        }

        phoneCode.getSelectedCode().observe(this, Observer { code ->
            if (code != viewModel.phoneModel.phoneCode.getNotNull()) {
                viewModel.phoneModel.phoneCode.set(code)
            }
        })

        phoneCode.touchListener = {
            hideKeyboard()
        }
    }

    override fun handleErrorMessage(message: PresentationMessage) {
        when(message.id) {
            Messages.EROR.getId() -> {
                showDefaultError(message.content!!)
            }
        }
    }
}
