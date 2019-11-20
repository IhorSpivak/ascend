package com.doneit.ascend.presentation.login.sign_up

import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import androidx.lifecycle.Observer
import com.doneit.ascend.presentation.login.R
import com.doneit.ascend.presentation.login.databinding.SignUpFragmentBinding
import com.doneit.ascend.presentation.login.utils.applyLinkStyle
import com.doneit.ascend.presentation.main.extensions.hideKeyboard
import com.vrgsoft.core.presentation.fragment.BaseFragment
import kotlinx.android.synthetic.main.group_phone.*
import kotlinx.android.synthetic.main.sign_up_fragment.*
import kotlinx.android.synthetic.main.toolbar.*
import org.kodein.di.generic.instance

class SignUpFragment : BaseFragment<SignUpFragmentBinding>() {

    override val viewModelModule = SignUpViewModelModule.get(this)
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
