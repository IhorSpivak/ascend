package com.doneit.ascend.presentation.main.ascension_plan

import androidx.lifecycle.LiveData
import com.doneit.ascend.domain.entity.user.UserEntity
import com.doneit.ascend.presentation.main.base.BaseViewModel

interface AscensionPlanContract {
    interface ViewModel : BaseViewModel {
        val user: LiveData<UserEntity?>

        fun createSpiritual()
        fun createGoal()
    }

    interface Router {
        fun navigateToSpiritualActionSteps()
        fun navigateToMyGoals()
        fun navigateToCreateSpiritualActionSteps()
        fun navigateToCreateGoal()
    }
}