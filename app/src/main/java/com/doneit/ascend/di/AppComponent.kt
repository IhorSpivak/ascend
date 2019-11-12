package com.doneit.ascend.di

import com.doneit.ascend.ui.login.LoginViewModel
import com.doneit.ascend.ui.signup.SignUpViewModel
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        NetworkModule::class
    ]
)
interface AppComponent {

    fun inject(model: LoginViewModel)

    fun inject(model: SignUpViewModel)
}
