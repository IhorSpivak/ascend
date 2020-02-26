package com.doneit.ascend.presentation.main.my_spiritual_action_steps.list

import androidx.lifecycle.LiveData
import com.doneit.ascend.presentation.main.base.argumented.ArgumentedViewModel
import com.doneit.ascend.presentation.main.my_spiritual_action_steps.list.common.SpiritualActionListArgs

interface SpiritualActionListContract {
    interface ViewModel: ArgumentedViewModel<SpiritualActionListArgs> {
        val spiritualActionList: LiveData<List<SpiritualActionStepEntity>>
        fun moveToCompleted(id: Int)
        fun editActionStep(id: Int)
    }

    interface Router{
        fun navigateToEditActionStep(id: Int)
    }
}