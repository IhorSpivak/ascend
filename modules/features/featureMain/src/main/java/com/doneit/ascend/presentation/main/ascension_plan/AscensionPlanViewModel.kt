package com.doneit.ascend.presentation.main.ascension_plan

import androidx.lifecycle.MutableLiveData
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


    override val isActionStepsVisible = MutableLiveData<Boolean>(true)
    override val isGoalsListVisible =  MutableLiveData<Boolean>(false)
    override val user = userUseCase.getUserLive()

    override fun createSpiritual() {
        router.navigateToCreateSpiritualActionSteps()
    }

    override fun setFilter(filterDTO: FilterDTO) {
        isActionStepsVisible.postValue(filterDTO.stepsEntity == StepsDTO.STEPS)
        isGoalsListVisible.postValue(filterDTO.stepsEntity == StepsDTO.GOALS)
    }
}