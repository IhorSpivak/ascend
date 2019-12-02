package com.doneit.ascend.presentation.login.utils

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.doneit.ascend.presentation.login.R
import com.facebook.CallbackManager
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.GoogleApiClient

object LoginHelper {
    var callbackManager = CallbackManager.Factory.create()

    fun getGoogleSignInClient(
        context: Context,
        activity: AppCompatActivity,
        connectionCallbacks: GoogleApiClient.ConnectionCallbacks,
        connectionFailedListener: GoogleApiClient.OnConnectionFailedListener
    ): GoogleApiClient {

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(context.getString(R.string.server_client_id))
            .requestServerAuthCode(context.getString(R.string.server_client_id))
            .requestProfile()
            .requestEmail()
            .build()

        return GoogleApiClient.Builder(activity, connectionCallbacks, connectionFailedListener)
            .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
            .build()
    }
}