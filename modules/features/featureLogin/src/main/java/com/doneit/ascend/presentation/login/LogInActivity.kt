package com.doneit.ascend.presentation.login

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.doneit.ascend.presentation.login.databinding.ActivityLoginBinding
import com.doneit.ascend.presentation.login.di.FeatureLoginModule
import com.vrgsoft.core.presentation.activity.BaseActivity
import org.kodein.di.generic.instance

class LogInActivity: BaseActivity() {
    override fun diModule() = FeatureLoginModule.get(this)

    private lateinit var binding: ActivityLoginBinding
    private val viewModel: LogInContract.ViewModel by instance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)

        /*viewModel.subscribe()
            .doOnSubscribe(::disposeOnDestroy)
            .subscribe {
                findNavController().navigate(R.id.signUpFragment)
            }*/
    }
}