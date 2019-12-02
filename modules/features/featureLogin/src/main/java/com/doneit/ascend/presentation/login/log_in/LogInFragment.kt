package com.doneit.ascend.presentation.login.log_in

import android.content.Intent
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import androidx.lifecycle.Observer
import com.doneit.ascend.presentation.login.R
import com.doneit.ascend.presentation.login.databinding.FragmentLoginBinding
import com.doneit.ascend.presentation.login.utils.LoginHelper
import com.doneit.ascend.presentation.login.utils.applyLinkStyle
import com.doneit.ascend.presentation.main.base.BaseFragment
import com.doneit.ascend.presentation.main.extensions.hideKeyboard
import com.facebook.AccessToken
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
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
            viewModel.loginModel.phoneCode.set(code)
        })

        phoneCode.touchListener = {
            hideKeyboard()
        }

        viewModel.facebookNeedLoginSubject.observe(this) {
            if (it != null && it) {

                val accessToken = AccessToken.getCurrentAccessToken()
                val isLoggedIn = accessToken != null && !accessToken.isExpired

                if(isLoggedIn) {
                    viewModel.onFacebookLogin(accessToken)
                }
                else {
                    binding.btnFB.callOnClick()
                }
            }
        }

        binding.btnFB.apply {

            fragment = this@LogInFragment

            registerCallback(LoginHelper.callbackManager, object : FacebookCallback<LoginResult?> {
                override fun onSuccess(result: LoginResult?) {
                    if(result != null) {
                        viewModel.onFacebookLogin(result.accessToken)
                    }
                    else {
                        // TODO: show error message
                    }
                }

                override fun onCancel() {

                }

                override fun onError(error: FacebookException?) {

                }
            })

            LoginManager.getInstance().logInWithReadPermissions(
                activity,
                listOf("email")
            )
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        LoginHelper.callbackManager?.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
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
        context!!.applyLinkStyle(spannable, 23, spannable.length)

        tvSocialTitle.text = spannable
        tvSocialTitle.movementMethod = LinkMovementMethod.getInstance()
    }
}