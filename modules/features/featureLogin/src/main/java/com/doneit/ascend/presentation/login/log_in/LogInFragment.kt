package com.doneit.ascend.presentation.login.log_in

import android.content.Intent
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import com.doneit.ascend.presentation.login.R
import com.doneit.ascend.presentation.login.databinding.FragmentLoginBinding
import com.doneit.ascend.presentation.login.utils.LoginHelper
import com.doneit.ascend.presentation.login.utils.applyLinkStyle
import com.doneit.ascend.presentation.main.base.BaseFragment
import com.doneit.ascend.presentation.main.extensions.hideKeyboard
import com.doneit.ascend.presentation.utils.Constants
import com.facebook.AccessToken
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.twitter.sdk.android.core.Callback
import com.twitter.sdk.android.core.TwitterCore
import com.twitter.sdk.android.core.TwitterException
import com.twitter.sdk.android.core.TwitterSession
import com.twitter.sdk.android.core.identity.TwitterAuthClient
import kotlinx.android.synthetic.main.fragment_login.*
import okhttp3.*
import org.json.JSONException
import org.json.JSONObject
import org.kodein.di.generic.instance
import java.io.IOException

class LogInFragment : BaseFragment<FragmentLoginBinding>() {

    override val viewModelModule = LogInViewModelModule.get(this)
    override val viewModel: LogInContract.ViewModel by instance()

    private val twitterAuthClient: TwitterAuthClient = TwitterAuthClient()

    override fun viewCreated(savedInstanceState: Bundle?) {

        binding.lifecycleOwner = this
        binding.model = viewModel

        initSignUpSpannable()

        binding.phoneLayout.phoneCode.getSelectedCode().observe(this, Observer { code ->
            viewModel.loginModel.phoneCode.set(code)
        })

        binding.phoneLayout.phoneCode.touchListener = {
            hideKeyboard()
        }

        viewModel.facebookNeedLoginSubject.observe(this) {
            if (it != null && it) {
                val accessToken = AccessToken.getCurrentAccessToken()
                val isLoggedIn = accessToken != null && !accessToken.isExpired

                if (isLoggedIn) {
                    viewModel.onFacebookLogin(accessToken)
                } else {
                    binding.btnFB.callOnClick()
                }
            }
        }

        viewModel.twitterNeedLoginSubject.observe(this) {
            if (it != null && it) {
                val twitterSession: TwitterSession? =
                    TwitterCore.getInstance().sessionManager.activeSession

                if (twitterSession == null) {
                    twitterAuthClient.authorize(activity!!, object : Callback<TwitterSession>() {
                        override fun failure(e: TwitterException?) {
                            Log.e("Twitter", "Failed to authenticate user " + e?.message)
                        }

                        override fun success(result: com.twitter.sdk.android.core.Result<TwitterSession>?) {
                            val session: TwitterSession = result?.data!!

                            val authToken = session.authToken
                            val token: String = authToken.token
                            val secret = authToken.secret

                            viewModel.loginWithTwitter(token, secret)
                        }
                    })
                } else {
                    val authToken = twitterSession.authToken
                    val token: String = authToken.token
                    val secret = authToken.secret

                    viewModel.loginWithTwitter(token, secret)
                }
            }
        }

        binding.btnFB.apply {
            setPermissions(listOf("email"))
            fragment = this@LogInFragment

            registerCallback(LoginHelper.callbackManager, object : FacebookCallback<LoginResult?> {
                override fun onSuccess(result: LoginResult?) {
                    if (result != null) {
                        viewModel.onFacebookLogin(result.accessToken)
                    } else {
                        Log.d("myLog", "Error")
                    }
                }

                override fun onCancel() {
                    Log.d("myLog", "Cancel")
                }

                override fun onError(error: FacebookException?) {
                    Log.d("myLog", "Error")
                }
            })
        }

        viewModel.user.observe(this, Observer {
            it?.let {
                tvSubtitle.text = it.community
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        btnTwitter.onActivityResult(requestCode, resultCode, data)
        LoginHelper.callbackManager?.onActivityResult(requestCode, resultCode, data)

        if (requestCode == Constants.RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }

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

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            val googleToken: String = account!!.serverAuthCode!!

            getAccessToken(googleToken)
        } catch (e: ApiException) {
            e.printStackTrace()
        }
    }

    private fun getAccessToken(authCode: String) {
        val client = OkHttpClient()

        val requestBody: RequestBody = FormBody.Builder()
            .add("grant_type", "authorization_code")
            .add("client_id", getString(R.string.server_client_id))
            .add("client_secret", getString(R.string.server_client_secret))
            .add("code", authCode)
            .build()

        val request: Request = Request.Builder()
            .url("https://www.googleapis.com/oauth2/v4/token")
            .header("Content-Type", "application/x-www-form-urlencoded")
            .post(requestBody)
            .build()

        client.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                try {
                    val jsonObject = JSONObject(response.body()?.string() ?: "")
                    val mAccessToken = jsonObject.get("access_token").toString()

                    viewModel.loginWithGoogle(mAccessToken)
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
        })
    }
}