package com.doneit.ascend.presentation.main.ascension_plan.create_spiritual

import androidx.lifecycle.MutableLiveData
import com.doneit.ascend.domain.entity.spiritual.SpiritualStepEntity
import com.doneit.ascend.presentation.main.base.BaseViewModelImpl
import com.doneit.ascend.presentation.models.ascension_plan.PresentationSpiritualStepModel
import com.doneit.ascend.presentation.utils.getNotNullString
import com.vrgsoft.annotations.CreateFactory
import com.vrgsoft.annotations.ViewModelDiModule
import kotlin.random.Random

@CreateFactory
@ViewModelDiModule
class CreateSpiritualViewModel(
    private val router: CreateSpiritualContract.Router
) : BaseViewModelImpl(), CreateSpiritualContract.ViewModel {

    override val addModel = PresentationSpiritualStepModel()
    override val steps = MutableLiveData<List<SpiritualStepEntity>>()

    override fun add() {
        val items = (steps.value ?: listOf()).toMutableList()
        items.add(
            SpiritualStepEntity(
                Random.nextLong(),
                addModel.title.getNotNullString(),
                addModel.repeat.getNotNullString(),
                addModel.timeCommitment.getNotNullString()
            )
        )
        steps.postValue(items)
        clear()
    }

    override fun clear() {
        addModel.title.set(EMPTY_STRING)
        addModel.repeat.set(EMPTY_STRING)
        addModel.timeCommitment.set(EMPTY_STRING)
    }

    override fun onBackClick() {
        router.onBack()
    }

    companion object {
        private const val EMPTY_STRING = ""
    }
}