package com.doneit.ascend.presentation.login.sign_up

import android.graphics.Typeface
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.StyleSpan
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.doneit.ascend.presentation.login.LogInActivity
import com.doneit.ascend.presentation.login.R
import com.doneit.ascend.presentation.login.databinding.FragmentSignUpBinding
import com.doneit.ascend.presentation.main.base.BaseFragment
import com.doneit.ascend.presentation.main.base.CommonViewModelFactory
import com.doneit.ascend.presentation.utils.applyLinkStyle
import com.doneit.ascend.presentation.utils.extensions.hideKeyboard
import com.doneit.ascend.presentation.utils.extensions.vmShared
import com.doneit.ascend.presentation.utils.getNotNullString
import kotlinx.android.synthetic.main.fragment_sign_up.*
import org.kodein.di.Kodein
import org.kodein.di.direct
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

class SignUpFragment : BaseFragment<FragmentSignUpBinding>() {

    override val viewModelModule = Kodein.Module(this::class.java.simpleName) {
        bind<ViewModelProvider.Factory>(tag = LogInActivity.SIGN_UP_VM_TAG) with singleton { CommonViewModelFactory(kodein.direct) }
        bind<ViewModel>(tag = SignUpViewModel::class.java.simpleName) with provider {
            SignUpViewModel(
                instance(),
                instance()
            )
        }
        bind<SignUpContract.ViewModel>() with provider { vmShared<SignUpViewModel>(instance(tag = LogInActivity.SIGN_UP_VM_TAG)) }
    }

    override val viewModel: SignUpContract.ViewModel by instance()

    override fun viewCreated(savedInstanceState: Bundle?) {
        binding.model = viewModel
        binding.executePendingBindings()
        viewModel.removeErrors()

        binding.toolbar.imBack.setOnClickListener {
            hideKeyboard()
            viewModel.onBackClick()
        }

        initSignInSpannable()

        binding.phoneLayout.phoneCode.getSelectedCode().observe(this, Observer { code ->
            if (code != viewModel.registrationModel.phoneCode.getNotNullString()) {
                viewModel.registrationModel.phoneCode.set(code)
            }
        })

        binding.phoneLayout.phoneCode.touchListener = {
            hideKeyboard()
        }
    }

    private fun setClickable(
        text: String,
        linkedText: String,
        ss: SpannableString,
        clickableTextHandler: Any
    ) {

        val startIndex = text.indexOf(linkedText)
        ss.setSpan(
            clickableTextHandler,
            startIndex,
            startIndex + linkedText.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        val boldSpan = StyleSpan(Typeface.BOLD)
        ss.setSpan(
            boldSpan,
            startIndex,
            startIndex + linkedText.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
    }

    private fun initSignInSpannable() {
        val spannable = SpannableString(getString(R.string.already_member))
        val clickableSpan = object : ClickableSpan() {
            override fun onClick(p0: View) {
                hideKeyboard()
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

        // terms & condition and privacy policy
        val text = getString(R.string.agreement)
        val ss = SpannableString(text)

        val termsLinkedText = getString(R.string.terms_link)
        val privacyLinkedText = getString(R.string.privacy_link)

        setClickable(text, termsLinkedText, ss, object : ClickableSpan() {
            override fun onClick(p0: View) {
                viewModel.onTermsAndConditionsClick()
            }

            override fun updateDrawState(ds: TextPaint) {
                ds.isUnderlineText = false
            }
        })
        setClickable(text, privacyLinkedText, ss, object : ClickableSpan() {
            override fun onClick(p0: View) {
                viewModel.onPrivacyPolicyClick()
            }

            override fun updateDrawState(ds: TextPaint) {
                ds.isUnderlineText = false
            }
        })

        tvCheckboxDescription.text = ss
        tvCheckboxDescription.movementMethod = LinkMovementMethod.getInstance()
    }
}
