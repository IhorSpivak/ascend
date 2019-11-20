package com.doneit.ascend.presentation.login.sign_up

import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.doneit.ascend.presentation.login.R
import com.doneit.ascend.presentation.login.databinding.SignUpFragmentBinding
import com.doneit.ascend.presentation.login.utils.applyLinkStyle
import com.doneit.ascend.presentation.main.base.CommonViewModelFactory
import com.doneit.ascend.presentation.main.extensions.hideKeyboard
import com.doneit.ascend.presentation.main.extensions.vmShared
import com.vrgsoft.core.presentation.fragment.BaseFragment
import kotlinx.android.synthetic.main.group_icon_edit.view.*
import kotlinx.android.synthetic.main.group_phone.*
import kotlinx.android.synthetic.main.sign_up_fragment.*
import kotlinx.android.synthetic.main.toolbar.*
import kotlinx.android.synthetic.main.view_edit_with_error.view.*
import org.kodein.di.Kodein
import org.kodein.di.direct
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

class SignUpFragment : BaseFragment<SignUpFragmentBinding>() {

    override val viewModelModule = Kodein.Module(this::class.java.simpleName){
        bind<ViewModelProvider.Factory>() with singleton { CommonViewModelFactory(kodein.direct) }
        bind<ViewModel>(tag = SignUpViewModel::class.java.simpleName) with provider { SignUpViewModel(instance(), instance()) }
        bind<SignUpContract.ViewModel>() with singleton { vmShared<SignUpViewModel>(instance()) }
    }

    override val viewModel: SignUpContract.ViewModel by instance()

    override fun viewCreated(savedInstanceState: Bundle?) {
        binding.model = viewModel

        imBack.setOnClickListener {
            viewModel.onBackClick()
        }

        initSignInSpannable()

        phoneCode.getSelectedCode().observe(this, Observer {code ->
            viewModel.registrationModel.code = code
        })

        phoneCode.touchListener = {
            hideKeyboard()
        }

        applyValidators()
    }

    private fun initSignInSpannable() {
        val spannable = SpannableString(getString(R.string.already_member))
        val clickableSpan = object : ClickableSpan() {
            override fun onClick(p0: View) {
                viewModel.onBackClick()
            }

            override fun updateDrawState(ds: TextPaint) {
                ds.isUnderlineText = false
            }
        }
        spannable.setSpan(clickableSpan, 18, spannable.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        context!!.applyLinkStyle(spannable, 18, spannable.length)

        signIn.text = spannable
        signIn.movementMethod = LinkMovementMethod.getInstance()
    }

    private fun applyValidators() {
        userName.editWithError.editText.setOnFocusChangeListener { _, _ ->
            validate()
        }
    }

    private fun validate() {
        var isValid = true
        isValid = isValid and validateName()


    }

    private fun validateName(): Boolean {
        return true
    }

    private fun validateEmail(): Boolean {
        return true
    }

    private fun validatePhone(): Boolean {
        return true
    }

    private fun validatePassword(): Boolean {
        return true
    }

    private fun validateConfirmPassword(): Boolean {
        return true
    }
}
