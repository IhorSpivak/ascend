package com.doneit.ascend.presentation.main.ascension_plan.create_goal

import androidx.databinding.ObservableField
import com.doneit.ascend.presentation.main.base.BaseViewModelImpl
import com.vrgsoft.annotations.CreateFactory
import com.vrgsoft.annotations.ViewModelDiModule

@CreateFactory
@ViewModelDiModule
class CreateGoalsViewModel(
    private val router: CreateGoalsContract.Router
) : BaseViewModelImpl(), CreateGoalsContract.ViewModel {

    override val counter = ObservableField(0)

    override fun plus() {
        counter.set(counter.get()!! + 1)
    }

    override fun minus() {
        val value = counter.get()!!
        if (value > 0) {
            counter.set(value - 1)
        }
    }

    override fun onBackClick() {
        router.onBack()
    }
}