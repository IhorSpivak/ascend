package com.doneit.ascend.presentation.main.ascension_plan

import androidx.lifecycle.LiveData
import com.doneit.ascend.domain.entity.user.UserEntity
import androidx.paging.PagedList
import com.doneit.ascend.domain.entity.ascension.AscensionEntity
import com.doneit.ascend.presentation.main.base.BaseViewModel
import com.doneit.ascend.presentation.models.ascension_plan.PresentationAscensionFilter

interface AscensionPlanContract {
    interface ViewModel : BaseViewModel {
        val user: LiveData<UserEntity?>
        val isActionStepsVisible: LiveData<Boolean>
        val isGoalsListVisible: LiveData<Boolean>
        val filter: LiveData<PresentationAscensionFilter>
        val data: LiveData<PagedList<AscensionEntity>>

        fun createSpiritual()
        fun setFilterModel(filter: PresentationAscensionFilter)
        fun createGoal()
    }

    interface Router {
        fun navigateToSpiritualActionSteps()
        fun navigateToMyGoals()
        fun navigateToCreateSpiritualActionSteps()
        fun navigateToCreateGoal()
    }
}