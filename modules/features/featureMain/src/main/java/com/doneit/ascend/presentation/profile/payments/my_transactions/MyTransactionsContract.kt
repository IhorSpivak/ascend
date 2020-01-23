package com.doneit.ascend.presentation.profile.payments.my_transactions

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.doneit.ascend.domain.entity.PurchaseEntity
import com.doneit.ascend.presentation.main.base.BaseViewModel

interface MyTransactionsContract {
    interface ViewModel: BaseViewModel {
        val transtactions: LiveData<PagedList<PurchaseEntity>>

        fun refetch()
    }

    interface Router {

    }
}