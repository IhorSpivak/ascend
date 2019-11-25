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
import com.doneit.ascend.presentation.login.databinding.FragmentSignUpBinding
import com.doneit.ascend.presentation.login.utils.applyLinkStyle
import com.doneit.ascend.presentation.login.utils.getNotNull
import com.doneit.ascend.presentation.main.base.BaseFragment
import com.doneit.ascend.presentation.main.base.CommonViewModelFactory
import com.doneit.ascend.presentation.main.extensions.hideKeyboard
import com.doneit.ascend.presentation.main.extensions.vmShared
import kotlinx.android.synthetic.main.fragment_sign_up.*
import kotlinx.android.synthetic.main.group_phone.*
import kotlinx.android.synthetic.main.toolbar.*
import org.kodein.di.Kodein
import org.kodein.di.direct
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

class SignUpFragment : BaseFragment<FragmentSignUpBinding>() {

    override val viewModelModule = Kodein.Module(this::class.java.simpleName){
        bind<ViewModelProvider.Factory>() with singleton { CommonViewModelFactory(kodein.direct) }
        bind<ViewModel>(tag = SignUpViewModel::class.java.simpleName) with provider { SignUpViewModel(instance(), instance()) }
        bind<SignUpContract.ViewModel>() with singleton { vmShared<SignUpViewModel>(instance()) }
    }

    override val viewModel: SignUpContract.ViewModel by instance()

    override fun viewCreated(savedInstanceState: Bundle?) {
        binding.model = viewModel
        binding.executePendingBindings()
        viewModel.removeErrors()

        imBack.setOnClickListener {
            viewModel.onBackClick()
        }

        initSignInSpannable()

        phoneCode.getSelectedCode().observe(this, Observer {code ->
            if(code != viewModel.registrationModel.phoneCode.getNotNull()) {
                viewModel.registrationModel.phoneCode.set(code)
            }
        })

        phoneCode.touchListener = {
            hideKeyboard()
        }
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
}
