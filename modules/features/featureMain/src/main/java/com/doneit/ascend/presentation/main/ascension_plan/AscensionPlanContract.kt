package com.doneit.ascend.presentation.main.ascension_plan

import androidx.lifecycle.LiveData
import com.doneit.ascend.domain.entity.UserEntity
import com.doneit.ascend.presentation.main.base.BaseViewModel

interface AscensionPlanContract {
        interface ViewModel : BaseViewModel {
            val user: LiveData<UserEntity?>

        }

        interface Router {
            fun navigateToSpiritualActionSteps()
            fun navigateToMyGoals()
            fun navigateToCreateSpiritualActionSteps()
            fun navigateToCreateGoal()
        }
}