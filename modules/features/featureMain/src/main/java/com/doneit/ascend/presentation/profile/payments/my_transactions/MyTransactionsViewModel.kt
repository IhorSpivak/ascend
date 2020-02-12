package com.doneit.ascend.presentation.profile.payments.my_transactions

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagedList
import com.doneit.ascend.domain.entity.PurchaseEntity
import com.doneit.ascend.domain.entity.dto.PurchasesDTO
import com.doneit.ascend.domain.entity.dto.SortType
import com.doneit.ascend.domain.use_case.interactor.purchase.PurchaseUseCase
import com.doneit.ascend.presentation.main.base.BaseViewModelImpl
import com.vrgsoft.annotations.CreateFactory
import com.vrgsoft.annotations.ViewModelDiModule
import kotlinx.coroutines.launch

@CreateFactory
@ViewModelDiModule
class MyTransactionsViewModel (
    private val purchaseUseCase: PurchaseUseCase,
    private val router: MyTransactionsContract.Router
): BaseViewModelImpl(), MyTransactionsContract.ViewModel {

    override val transtactions = MutableLiveData<PagedList<PurchaseEntity>>()

    init {
        refetch()
    }

    override fun refetch() {
        viewModelScope.launch {
            val result = purchaseUseCase.getPurchases(
                PurchasesDTO(
                    sortColumn = PurchaseEntity.CREATED_AT_KEY,
                    sortType = SortType.DESC
                )
            )

            transtactions.postValue(result)
        }
    }
}