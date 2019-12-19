package com.doneit.ascend.presentation.main.master_mind_profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.doneit.ascend.domain.entity.MasterMindEntity
import com.doneit.ascend.domain.use_case.interactor.master_mind.MasterMindUseCase
import com.doneit.ascend.domain.use_case.interactor.user.UserUseCase
import com.doneit.ascend.presentation.main.base.BaseViewModelImpl
import com.doneit.ascend.presentation.main.create_group.select_group_type.SelectGroupTypeContract
import com.doneit.ascend.presentation.utils.extensions.toErrorMessage
import kotlinx.coroutines.launch

class MMProfileViewModel(
    private val router: MMProfileContract.Router,
    private val userUseCase: UserUseCase,
    private val masterMindUseCase: MasterMindUseCase
) : BaseViewModelImpl(), MMProfileContract.ViewModel {

    override val profile = MutableLiveData<MasterMindEntity>()


    override fun loadData(id: Long) {
        viewModelScope.launch {
            val response = masterMindUseCase.getProfile(id)

            if (response.isSuccessful) {
                profile.postValue(response.successModel!!)
            }
        }
    }

    override fun report(content: String) {
        viewModelScope.launch {
            profile.value?.let {
                val res = userUseCase.report(content, it.id)
                if(res.isSuccessful.not()){
                    showDefaultErrorMessage(res.errorModel!!.toErrorMessage())
                }
            }
        }
    }

    override fun goBack() {
        router.closeActivity()
    }
}