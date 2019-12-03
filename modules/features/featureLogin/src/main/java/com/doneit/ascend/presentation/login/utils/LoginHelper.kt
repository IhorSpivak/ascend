package com.doneit.ascend.presentation.login.utils

import android.content.Context
import com.doneit.ascend.presentation.login.R
import com.facebook.CallbackManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions

object LoginHelper {
    var callbackManager = CallbackManager.Factory.create()

    fun getGoogleSignInClient(context: Context): GoogleSignInClient {

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(context.getString(R.string.server_client_id))
            .requestServerAuthCode(context.getString(R.string.server_client_id))
            .requestProfile()
            .requestEmail()
            .build()

        return GoogleSignIn.getClient(context, gso)
    }
}