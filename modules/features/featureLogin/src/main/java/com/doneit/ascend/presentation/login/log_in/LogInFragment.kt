package com.doneit.ascend.presentation.login.log_in

import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.doneit.ascend.presentation.login.R
import com.doneit.ascend.presentation.login.databinding.FragmentLoginBinding
import com.vrgsoft.core.presentation.fragment.BaseFragment
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.group_phone.*
import org.kodein.di.generic.instance

class LogInFragment : BaseFragment<FragmentLoginBinding>() {

    override val viewModelModule = LogInViewModelModule.get(this)
    override val viewModel: LogInContract.ViewModel by instance()

    override fun viewCreated(savedInstanceState: Bundle?) {
        binding.lifecycleOwner = this
        binding.model = viewModel

        initSignUpSpannable()

        phoneCode.getSelectedCode().observe(this, Observer { code ->
            viewModel.loginModel.phoneCode = code
        })
    }

    private fun initSignUpSpannable() {
        val spannable = SpannableString(getString(R.string.social_title))
        val clickableSpan = object : ClickableSpan() {
            override fun onClick(p0: View) {
                viewModel.signUpClick()
            }

            override fun updateDrawState(ds: TextPaint) {
                ds.isUnderlineText = false
            }
        }
        spannable.setSpan(clickableSpan, 23, spannable.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        val color = ContextCompat.getColor(context!!, R.color.defaultTextColor)
        spannable.setSpan(
            ForegroundColorSpan(color),
            23,
            spannable.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        tvSocialTitle.text = spannable
        tvSocialTitle.movementMethod = LinkMovementMethod.getInstance()
    }
}