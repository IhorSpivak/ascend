package com.doneit.ascend.presentation.main.ascension_plan.goals.list

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.doneit.ascend.domain.entity.ascension.goal.GoalEntity
import com.doneit.ascend.presentation.main.ascension_plan.goals.list.common.GoalsListArgs
import com.doneit.ascend.presentation.main.base.argumented.ArgumentedViewModel

interface GoalsListContract {
    interface ViewModel: ArgumentedViewModel<GoalsListArgs> {
        val spiritualActionList: LiveData<PagedList<GoalEntity>>
        fun moveToCompleted(goal: GoalEntity)
        fun editActionStep(goal: GoalEntity)
    }

    interface Router{
        fun navigateToEditGoal(goal: GoalEntity)
    }
}