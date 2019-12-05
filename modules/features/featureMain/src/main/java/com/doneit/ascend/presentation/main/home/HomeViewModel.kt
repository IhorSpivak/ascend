package com.doneit.ascend.presentation.main.home

import com.doneit.ascend.domain.entity.UserEntity
import com.doneit.ascend.presentation.main.base.BaseViewModelImpl
import com.doneit.ascend.presentation.utils.LocalStorage
import com.vrgsoft.annotations.CreateFactory
import com.vrgsoft.annotations.ViewModelDiModule

@CreateFactory
@ViewModelDiModule
class HomeViewModel(
    private val localStorage: LocalStorage
) : BaseViewModelImpl(), HomeContract.ViewModel {

    override fun getUser(): UserEntity {
        return localStorage.loadUser()
    }
}