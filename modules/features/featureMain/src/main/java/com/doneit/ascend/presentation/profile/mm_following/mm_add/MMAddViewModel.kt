package com.doneit.ascend.presentation.profile.mm_following.mm_add

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagedList
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

    override val masterMinds = MutableLiveData<PagedList<MasterMindEntity>>()
    private var lastRequest = DEFAULT_REQUEST

    init {
        submitRequest(lastRequest)
    }

    override fun submitRequest(name: String) {
        lastRequest = name
        viewModelScope.launch {
            val result = masterMindUseCase.getMasterMingListToAdd(name)
            masterMinds.postValue(result)
        }
    }

    override fun follow(id: Long) {
        viewModelScope.launch {
            val result = masterMindUseCase.follow(id)

            if (result.isSuccessful) {
                submitRequest(lastRequest)
            } else {
                showDefaultErrorMessage(result.errorModel!!.toErrorMessage())
            }
        }
    }

    override fun onBackClick() {
        router.goBack()
    }

    companion object {
        private const val DEFAULT_REQUEST = ""
    }
}