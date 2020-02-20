package com.doneit.ascend.presentation.profile.mm_following.mm_add

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import com.doneit.ascend.domain.entity.MasterMindEntity
import com.doneit.ascend.domain.use_case.interactor.master_mind.MasterMindUseCase
import com.doneit.ascend.presentation.main.base.BaseViewModelImpl
import com.doneit.ascend.presentation.utils.extensions.toErrorMessage
import com.vrgsoft.annotations.CreateFactory
import com.vrgsoft.annotations.ViewModelDiModule
import kotlinx.coroutines.launch

@CreateFactory
@ViewModelDiModule
class MMAddViewModel(
    private val router: MMAddContract.Router,
    private val masterMindUseCase: MasterMindUseCase
) : BaseViewModelImpl(), MMAddContract.ViewModel {

    private val mmName = MutableLiveData<String>()
    override val masterMinds = mmName.switchMap { masterMindUseCase.getMMListByName(it) }

    init {
        submitRequest(DEFAULT_REQUEST)
    }

    override fun submitRequest(name: String) {
        mmName.postValue(name)
    }

    override fun follow(id: Long) {
        viewModelScope.launch {
            val result = masterMindUseCase.follow(id)

            if (result.isSuccessful.not()) {
                showDefaultErrorMessage(result.errorModel!!.toErrorMessage())
            }
        }
    }

    override fun openInfo(entity: MasterMindEntity) {
        router.navigateToMMInfo(entity.id)
    }

    override fun onBackClick() {
        router.onBack()
    }

    companion object {
        private const val DEFAULT_REQUEST = ""
    }
}