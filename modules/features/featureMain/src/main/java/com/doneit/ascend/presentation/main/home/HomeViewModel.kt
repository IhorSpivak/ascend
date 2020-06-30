package com.doneit.ascend.presentation.main.home

import com.doneit.ascend.domain.entity.user.Community
import com.doneit.ascend.domain.use_case.interactor.user.UserUseCase
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.base.BaseViewModelImpl
import com.vrgsoft.annotations.CreateFactory
import com.vrgsoft.annotations.ViewModelDiModule

@CreateFactory
@ViewModelDiModule
class HomeViewModel(
    private val userUseCase: UserUseCase,
    private val router: HomeContract.Router
) : BaseViewModelImpl(), HomeContract.ViewModel {

    override val user = userUseCase.getUserLive()

    override fun getListOfTitles(): List<Int> {
        user.value?.let {
            return when (it.community) {
                Community.SUCCESS.title,
                Community.INDUSTRY.title -> listOf(
                    R.string.daily,
                    R.string.webinars,
                    R.string.collaboration,
                    R.string.master_mind
                )
                Community.FITNESS.title  -> listOf(
                    R.string.daily,
                    R.string.webinars,
                    R.string.collaboration,
                    R.string.coaching
                )
                Community.RECOVERY.title  -> listOf(
                    R.string.daily,
                    R.string.webinars,
                    R.string.groups,
                    R.string.master_mind
                )
                Community.FAMILY.title  -> listOf(
                    R.string.daily,
                    R.string.webinars,
                    R.string.groups,
                    R.string.coaching
                )
                Community.SPIRITUAL.title  -> listOf(
                    R.string.daily,
                    R.string.webinars,
                    R.string.collaboration,
                    R.string.coaching
                )
                else -> throw IllegalArgumentException("Unknown community: ${it.community}")
            }
        }
        return emptyList()
    }
}