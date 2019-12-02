package com.doneit.ascend.presentation.login.log_in

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.IntentSender
import android.os.AsyncTask
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.TextUtils
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import com.doneit.ascend.presentation.login.R
import com.doneit.ascend.presentation.login.databinding.FragmentLoginBinding
import com.doneit.ascend.presentation.login.utils.LoginHelper
import com.doneit.ascend.presentation.login.utils.LoginListener
import com.doneit.ascend.presentation.login.utils.applyLinkStyle
import com.doneit.ascend.presentation.main.base.BaseFragment
import com.doneit.ascend.presentation.main.extensions.hideKeyboard
import com.facebook.AccessToken
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.google.android.gms.auth.GoogleAuthException
import com.google.android.gms.auth.GoogleAuthUtil
import com.google.android.gms.auth.UserRecoverableAuthException
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignInResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.common.Scopes
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.Scope
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.group_phone.*
import org.kodein.di.generic.instance
import java.io.IOException
import java.util.*

class LogInFragment : BaseFragment<FragmentLoginBinding>(), LoginListener {

    override val viewModelModule = LogInViewModelModule.get(this)
    override val viewModel: LogInContract.ViewModel by instance()

    private var mGoogleApiClientForSignIn: GoogleApiClient? = null
    private var mGoogleAccount: GoogleSignInAccount? = null

    private var newUserToken: String = DEFAULT_TOKEN

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

                if (isLoggedIn) {
                    viewModel.onFacebookLogin(accessToken)
                } else {
                    binding.btnFB.callOnClick()
                }
            }
        }

        viewModel.googleNeedLoginSubject.observe(this) {
            if (it != null && it) {
                startGoogleAuth()
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
                        // TODO: show error message
                    }
                }

                override fun onCancel() {
                    Log.e("myLog", "Cancel")
                }

                override fun onError(error: FacebookException?) {
                    Log.e("myLog", "Error")
                }
            })
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        LoginHelper.callbackManager?.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK) {

            when (requestCode) {
                GOOGLE_PLUS_SIGN_IN_CODE -> {
                    val result = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
                    handleSignInResult(result)
                }
                REQUEST_CODE_RESOLUTION -> {
                    mGoogleApiClientForSignIn!!.connect()
                }
                REQUEST_AUTHORIZATION -> googleExchangeCodeToken
            }
        } else {
            if (requestCode == GOOGLE_PLUS_SIGN_IN_CODE) {
                Log.d("onActivityResult", "GoogleSignIn error")
            }
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

    override fun loginWithGoogle(idToken: String?, displayName: String?) {
        viewModel.loginWithGoogle(idToken!!)
    }

    fun startGoogleAuth() {
        newUserToken = DEFAULT_TOKEN

        val gso = GoogleSignInOptions.Builder(
            GoogleSignInOptions.DEFAULT_SIGN_IN
        )
            .requestScopes(
                Scope(Scopes.PROFILE),
                Scope(Scopes.DRIVE_FULL),
                Scope(Scopes.DRIVE_FILE),
                Scope(Scopes.DRIVE_APPFOLDER)
            )
            .requestIdToken(getString(R.string.server_client_id))
            .requestServerAuthCode(getString(R.string.server_client_id), false)
            .requestProfile()
            .requestEmail()
            .build()
        mGoogleApiClientForSignIn =
            GoogleApiClient.Builder(activity!!,
                object : GoogleApiClient.ConnectionCallbacks {
                    override fun onConnected(bundle: Bundle?) {
                        Log.i(TAG, "GoogleApiClient connected")

                        if (newUserToken != DEFAULT_TOKEN) {
                            // TODO SEND TOKEN
                            viewModel.loginWithGoogle(newUserToken)
                        }
                    }

                    override fun onConnectionSuspended(i: Int) {
                        Log.i(TAG, "GoogleApiClient connection suspended")
                    }
                },
                GoogleApiClient.OnConnectionFailedListener { connectionResult ->
                    Log.i(TAG, "GoogleApiClient connection failed: $connectionResult")

                    if (!connectionResult.hasResolution()) {
                        GoogleApiAvailability.getInstance().getErrorDialog(
                            activity, connectionResult.errorCode, 0
                        ).show()

                        return@OnConnectionFailedListener
                    }
                    try {
                        connectionResult.startResolutionForResult(
                            activity,
                            REQUEST_CODE_RESOLUTION
                        )
                    } catch (e: IntentSender.SendIntentException) {
                        Log.e(TAG, "Exception while starting resolution activity", e)
                    }
                })
                .addApi(
                    Auth.GOOGLE_SIGN_IN_API,
                    gso
                )
                .build()

        (mGoogleApiClientForSignIn as GoogleApiClient).connect()

        val signInIntent =
            Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClientForSignIn)

        try {
            startActivityForResult(signInIntent, GOOGLE_PLUS_SIGN_IN_CODE)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun handleSignInResult(result: GoogleSignInResult) {
        Log.d("GoogleSignIn", "handleSignInResult:" + result.isSuccess)

        if (result.isSuccess) {
            Log.d("GoogleSignIn", "result.isSuccess()")

            // Signed in successfully, show authenticated UI.
            mGoogleAccount = result.signInAccount
            val googleToken = mGoogleAccount!!.idToken
            Log.d("GoogleSignInToken=", googleToken)

            if (newUserToken == "-1") {
                //saveUserData(mGoogleAccount)
                googleExchangeCodeToken
            } else {
                //goToMainActivity()
            }
        } else { // Signed out, show unauthenticated UI.
            Log.d("GoogleSignIn", "Signed out, show unauthenticated UI.")
        }
    }

    private val googleExchangeCodeToken: Unit
        private get() {
            if (mGoogleAccount == null || mGoogleAccount!!.account == null) {
                return
            }

            val task: AsyncTask<Void?, Void?, String?> =
                object : AsyncTask<Void?, Void?, String?>() {

                    override fun onPostExecute(token: String?) {
                        if (token != null) {
                            /* Successfully got ExchangeCode token*/
//                            Util.saveGoogleExchangeCodeTokenToPrefs(token)
                            Log.d("GoogleExchangeToken= ", token)

                            // TODO SEND TOKEN
                            viewModel.loginWithGoogle(token)
                        }
                    }

                    override fun doInBackground(vararg p0: Void?): String? {
                        var tokenForSos: String? = null

                        val SCOPES =
                            Arrays.asList(
                                *arrayOf(
                                    "https://www.googleapis.com/auth/plus.login"
                                )
                            )
                        val scopeForSos = String.format(
                            "oauth2:server:client_id:%s:api_scope:%s",
                            "782179262877-blgh224u0eldj7ck2e15t8cc7pc0u18j.apps.googleusercontent.com",
                            TextUtils.join(" ", SCOPES)
                        )
                        try {
                            tokenForSos = GoogleAuthUtil.getToken(
                                activity,
                                mGoogleAccount!!.account,
                                scopeForSos
                            )
                        } catch (e: GoogleAuthException) {
                            e.printStackTrace()
                            if (e is UserRecoverableAuthException) {
                                startActivityForResult(
                                    e.intent,
                                    REQUEST_AUTHORIZATION
                                )
                            }
                        } catch (e: IOException) {
                            e.printStackTrace()
                        }

                        return tokenForSos
                    }
                }
            task.execute()
        }

    companion object {
        private const val DEFAULT_TOKEN = "-1"
        private const val TAG = "myLog"
        private const val GOOGLE_PLUS_SIGN_IN_CODE = 2
        private const val REQUEST_CODE_RESOLUTION = 1
        private const val REQUEST_AUTHORIZATION = 200
    }
}