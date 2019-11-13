package com.vrgsoft.carButler.domain.use_case.di

import com.vrgsoft.carButler.domain.use_case.interactor.user.UserInteractor
import com.vrgsoft.carButler.domain.use_case.interactor.user.UserUseCase
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider

object UseCaseModule {
    fun get() = Kodein.Module("UseCaseModule") {
        bind<UserUseCase>() with provider {
            UserInteractor(
                instance()
            )
        }
    }
}