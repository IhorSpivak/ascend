package com.doneit.ascend.presentation.main.spiritual_action_steps.list

import androidx.lifecycle.*
import androidx.paging.DataSource
import androidx.paging.ItemKeyedDataSource
import androidx.paging.PagedList
import androidx.paging.toLiveData
import com.doneit.ascend.domain.entity.RepeatType
import com.doneit.ascend.domain.entity.SpiritualActionStepEntity
import com.doneit.ascend.domain.entity.TimeCommitmentEntity
import com.doneit.ascend.domain.entity.TimeCommitmentType
import com.doneit.ascend.presentation.main.base.BaseViewModelImpl
import com.doneit.ascend.presentation.main.spiritual_action_steps.list.common.SpiritualActionListArgs
import com.vrgsoft.annotations.CreateFactory
import com.vrgsoft.annotations.ViewModelDiModule
import java.util.concurrent.Executors

@CreateFactory
@ViewModelDiModule
class SpiritualActionListViewModel(
    //private val spiritualActionUseCase: SpiritualActionUseCase,
    private val router: SpiritualActionListContract.Router
) : BaseViewModelImpl(), SpiritualActionListContract.ViewModel {

    override val spiritualActionList: LiveData<PagedList<SpiritualActionStepEntity>>
        get() = isCompleted.switchMap {
            val config = PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setPageSize(10)
                .build()
            SourceFactory().toLiveData(config, fetchExecutor = Executors.newSingleThreadExecutor())
        }

    private var isCompleted = MutableLiveData<Boolean>()

    override fun applyArguments(args: SpiritualActionListArgs) {
        isCompleted.postValue(args.isCompleted)
    }

    override fun moveToCompleted(actionStep: SpiritualActionStepEntity) {
        //spiritualActionUseCase.moveToCompleted(actionStep)
    }

    override fun editActionStep(actionStep: SpiritualActionStepEntity) {
        router.navigateToEditActionStep(actionStep)
    }

    inner class Source: ItemKeyedDataSource<Int, SpiritualActionStepEntity>()  {
        override fun loadBefore(
            params: LoadParams<Int>,
            callback: LoadCallback<SpiritualActionStepEntity>
        ) {

        }

        override fun loadInitial(
            params: LoadInitialParams<Int>,
            callback: LoadInitialCallback<SpiritualActionStepEntity>
        ) {
            val list = arrayListOf<SpiritualActionStepEntity>()
            for (i in 1..10){
                list += SpiritualActionStepEntity(i,"Name", false, TimeCommitmentEntity(TimeCommitmentType.MINUTE,15), 1582800063344, RepeatType.DAY, SpiritualActionStepEntity.RepeatDayEntity(10),null,null)
            }
            callback.onResult(list)
        }

        override fun loadAfter(
            params: LoadParams<Int>,
            callback: LoadCallback<SpiritualActionStepEntity>
        ) {
            val list = arrayListOf<SpiritualActionStepEntity>()
            for (i in 1..10){
                list += SpiritualActionStepEntity(i,"Name", false, TimeCommitmentEntity(TimeCommitmentType.MINUTE,15), 1582800063344, RepeatType.DAY, SpiritualActionStepEntity.RepeatDayEntity(10),null,null)
            }
            callback.onResult(list)
        }

        override fun getKey(item: SpiritualActionStepEntity): Int {
            return item.id
        }
    }

    inner class SourceFactory : DataSource.Factory<Int, SpiritualActionStepEntity>() {
        override fun create(): DataSource<Int, SpiritualActionStepEntity> {
            return Source()
        }
    }
}