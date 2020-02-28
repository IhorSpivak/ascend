package com.doneit.ascend.presentation.main.ascension_plan

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import com.doneit.ascend.domain.entity.dto.ascension_plan.StepsDTO
import com.doneit.ascend.domain.use_case.interactor.ascention_plan.AscensionUseCase
import com.doneit.ascend.domain.use_case.interactor.user.UserUseCase
import com.doneit.ascend.presentation.main.base.BaseViewModelImpl
import com.doneit.ascend.presentation.models.ascension_plan.PresentationAscensionFilter
import com.doneit.ascend.presentation.models.ascension_plan.PresentationDateRange
import com.doneit.ascend.presentation.models.ascension_plan.toEntity
import com.vrgsoft.annotations.CreateFactory
import com.vrgsoft.annotations.ViewModelDiModule

@CreateFactory
@ViewModelDiModule
class AscensionPlanViewModel(
    private val userUseCase: UserUseCase,
    private val ascensionUseCase: AscensionUseCase,
    private val router: AscensionPlanContract.Router
) : BaseViewModelImpl(), AscensionPlanContract.ViewModel {
    override fun goToG() {
        router.navigateToMyGoals()
    }

    override fun goToSA() {
        router.navigateToSpiritualActionSteps()
    }

    override val filter = MutableLiveData<PresentationAscensionFilter>(
        PresentationAscensionFilter(
            PresentationDateRange.TODAY,
            StepsDTO.ALL
        )
    )
    override val data = filter.switchMap { ascensionUseCase.getListPaged(it.toEntity()) }
    override val isActionStepsVisible = MutableLiveData<Boolean>(true)
    override val isGoalsListVisible = MutableLiveData<Boolean>(true)
    override val user = userUseCase.getUserLive()

    override fun setFilterModel(filter: PresentationAscensionFilter) {
        this.filter.postValue(filter)

        val isAllVisible = filter.steps == StepsDTO.ALL
        isActionStepsVisible.postValue(isAllVisible || filter.steps == StepsDTO.SIRITUAL_STEPS)
        isGoalsListVisible.postValue(isAllVisible || filter.steps == StepsDTO.GOALS)
    }

    override fun createSpiritual() {
        router.navigateToCreateSpiritualActionSteps()
    }

    override fun createGoal() {
        router.navigateToCreateGoal()
    }
}