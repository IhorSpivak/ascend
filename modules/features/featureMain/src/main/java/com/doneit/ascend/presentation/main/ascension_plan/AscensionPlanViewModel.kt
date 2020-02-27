package com.doneit.ascend.presentation.main.ascension_plan

import androidx.lifecycle.MutableLiveData
import com.doneit.ascend.domain.entity.dto.DateRangeDTO
import com.doneit.ascend.domain.entity.dto.FilterDTO
import com.doneit.ascend.domain.entity.dto.StepsDTO
import com.doneit.ascend.domain.use_case.interactor.user.UserUseCase
import com.doneit.ascend.presentation.main.base.BaseViewModelImpl
import com.vrgsoft.annotations.CreateFactory
import com.vrgsoft.annotations.ViewModelDiModule

@CreateFactory
@ViewModelDiModule
class AscensionPlanViewModel(
    private val userUseCase: UserUseCase,
    private val router: AscensionPlanContract.Router
) : BaseViewModelImpl(), AscensionPlanContract.ViewModel {

    override var filter: FilterDTO = FilterDTO(DateRangeDTO.TODAY, StepsDTO.ALL)
        set(value) {
            field = value

            val isAllVisible = field.steps == StepsDTO.ALL
            isActionStepsVisible.postValue(isAllVisible || field.steps == StepsDTO.SIRITUAL_STEPS)
            isGoalsListVisible.postValue(isAllVisible || field.steps == StepsDTO.GOALS)
        }
    override val isActionStepsVisible = MutableLiveData<Boolean>(true)
    override val isGoalsListVisible =  MutableLiveData<Boolean>(true)
    override val user = userUseCase.getUserLive()

    override fun createSpiritual() {
        router.navigateToCreateSpiritualActionSteps()
    }
}