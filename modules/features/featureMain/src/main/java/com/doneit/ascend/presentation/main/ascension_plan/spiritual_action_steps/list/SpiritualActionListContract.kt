package com.doneit.ascend.presentation.main.ascension_plan.spiritual_action_steps.list

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.doneit.ascend.domain.entity.ascension.spiritual_action_step.SpiritualActionStepEntity
import com.doneit.ascend.presentation.main.base.argumented.ArgumentedViewModel
import com.doneit.ascend.presentation.main.ascension_plan.spiritual_action_steps.list.common.SpiritualActionListArgs

interface SpiritualActionListContract {
    interface ViewModel: ArgumentedViewModel<SpiritualActionListArgs> {
        val spiritualActionList: LiveData<PagedList<SpiritualActionStepEntity>>
        fun moveToCompleted(actionStep: SpiritualActionStepEntity)
        fun editActionStep(actionStep: SpiritualActionStepEntity)
    }

    interface Router{
        fun navigateToEditActionStep(actionStep: SpiritualActionStepEntity)
    }
}