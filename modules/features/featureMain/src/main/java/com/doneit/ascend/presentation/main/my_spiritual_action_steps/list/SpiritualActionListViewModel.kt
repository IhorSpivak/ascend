package com.doneit.ascend.presentation.main.my_spiritual_action_steps.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import com.doneit.ascend.presentation.main.base.BaseViewModelImpl
import com.doneit.ascend.presentation.main.master_mind.list.common.ListArgs
import com.doneit.ascend.presentation.main.my_spiritual_action_steps.list.common.SpiritualActionListArgs
import com.vrgsoft.annotations.CreateFactory
import com.vrgsoft.annotations.ViewModelDiModule

@CreateFactory
@ViewModelDiModule
class SpiritualActionListViewModel(
    private val spiritualActionUseCase: SpiritualActionUseCase,
    private val router: SpiritualActionListContract.Router
) : BaseViewModelImpl(), SpiritualActionListContract.ViewModel{

    override val spiritualActionList: LiveData<List<SpiritualActionStepEntity>>
        get() = isCompleted.switchMap {
            spiritualActionUseCase.getActionList(it)
        }

    private var isCompleted = MutableLiveData<Boolean>()

    override fun applyArguments(args: SpiritualActionListArgs) {
        isCompleted.postValue(args.isCompleted)
    }

    override fun moveToCompleted(id: Int) {
        spiritualActionUseCase.moveToCompleted(id)
    }

    override fun editActionStep(id: Int) {
        router.navigateToEditActionStep(id)
    }
}