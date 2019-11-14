package com.doneit.ascend.presentation.login

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.vrgsoft.carButler.presentation.login.databinding.ActivityLoginBinding
import com.vrgsoft.core.presentation.activity.BaseActivity
import org.kodein.di.Kodein
import org.kodein.di.generic.instance

class LogInActivity: BaseActivity() {
    override fun diModule() = Kodein.Module("SplashActivity") {
    }

    private val router: LogInContract.Router by instance()
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)

    }
}