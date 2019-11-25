package com.doneit.ascend.presentation.utils

import android.content.Context
import android.net.ConnectivityManager
import androidx.lifecycle.MutableLiveData
import java.util.*

class ConnectionObserver(val context: Context) {

    val networkStateChanged: MutableLiveData<Boolean> = MutableLiveData(true)

    private var isNetworkAvailable: Boolean = true
        set(value) {
            if (field != value) {
                field = value;
                networkStateChanged.postValue(field)
            }
        }

    init {
        Timer().schedule(object : TimerTask() {
            override fun run() {
                isNetworkAvailable = checkInternetConnection(context)
            }
        }, Constants.TIME_CHECK_NETWORK)
    }

    companion object {
        fun checkInternetConnection(context: Context): Boolean {
            val connectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkInfo = connectivityManager.activeNetworkInfo

            return networkInfo != null && networkInfo.isConnected
        }
    }
}