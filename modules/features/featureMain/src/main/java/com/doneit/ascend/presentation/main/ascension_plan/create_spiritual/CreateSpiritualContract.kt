package com.doneit.ascend.presentation.main.ascension_plan.create_spiritual

import androidx.lifecycle.LiveData
import com.doneit.ascend.domain.entity.ascension.SpiritualStepEntity
import com.doneit.ascend.presentation.main.base.BaseViewModel
import com.doneit.ascend.presentation.models.ascension_plan.PresentationSpiritualStepModel

interface CreateSpiritualContract {
    interface ViewModel: BaseViewModel {
        val addModel: PresentationSpiritualStepModel
        val steps: LiveData<List<SpiritualStepEntity>>

        fun add()
        fun clear()
        fun onBackClick()
    }

    interface Router {
        fun onBack()
    }
}