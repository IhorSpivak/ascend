package com.doneit.ascend.presentation.main.ascension_plan.goals.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import androidx.paging.DataSource
import androidx.paging.ItemKeyedDataSource
import androidx.paging.PagedList
import androidx.paging.toLiveData
import com.doneit.ascend.domain.entity.ascension.goal.GoalDurationEntity
import com.doneit.ascend.domain.entity.ascension.goal.GoalDurationType
import com.doneit.ascend.domain.entity.ascension.goal.GoalEntity
import com.doneit.ascend.domain.entity.ascension.goal.GoalType
import com.doneit.ascend.domain.entity.ascension.spiritual_action_step.RepeatType
import com.doneit.ascend.domain.entity.ascension.spiritual_action_step.SpiritualActionStepEntity
import com.doneit.ascend.domain.entity.ascension.spiritual_action_step.TimeCommitmentEntity
import com.doneit.ascend.domain.entity.ascension.spiritual_action_step.TimeCommitmentType
import com.doneit.ascend.presentation.main.ascension_plan.goals.list.common.GoalsListArgs
import com.doneit.ascend.presentation.main.base.BaseViewModelImpl
import com.vrgsoft.annotations.CreateFactory
import com.vrgsoft.annotations.ViewModelDiModule
import java.util.concurrent.Executors

@CreateFactory
@ViewModelDiModule
class GoalsListViewModel (
    //private val goalUseCase: GoalUseCase,
    private val router: GoalsListContract.Router
) : BaseViewModelImpl(), GoalsListContract.ViewModel {

    override val spiritualActionList: LiveData<PagedList<GoalEntity>>
        get() = isCompleted.switchMap {
            val config = PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setPageSize(10)
                .build()
            SourceFactory().toLiveData(config, fetchExecutor = Executors.newSingleThreadExecutor())
        }

    private var isCompleted = MutableLiveData<Boolean>()

    override fun applyArguments(args: GoalsListArgs) {
        isCompleted.postValue(args.isCompleted)
    }

    override fun moveToCompleted(goal: GoalEntity) {
        //spiritualActionUseCase.moveToCompleted(actionStep)
    }

    override fun editActionStep(goal: GoalEntity) {
        router.navigateToEditGoal(goal)
    }

    inner class Source: ItemKeyedDataSource<Int, GoalEntity>()  {
        override fun loadBefore(
            params: LoadParams<Int>,
            callback: LoadCallback<GoalEntity>
        ) {

        }

        override fun loadInitial(
            params: LoadInitialParams<Int>,
            callback: LoadInitialCallback<GoalEntity>
        ) {
            val list = getItems()
            callback.onResult(list)
        }

        override fun loadAfter(
            params: LoadParams<Int>,
            callback: LoadCallback<GoalEntity>
        ) {
            val list = getItems()

            callback.onResult(list)
        }

        override fun getKey(item: GoalEntity): Int {
            return item.id
        }
        private fun getItems(): ArrayList<GoalEntity>{
            val list = arrayListOf<GoalEntity>()
            for (i in 1..10){
                list += GoalEntity(
                    id = i,
                    name = "Goal Name",
                    actionStepList = getItemsChild(),
                    type = GoalType.ALL,
                    duration = GoalDurationEntity(GoalDurationType.MONTHS, 3)
                )
            }
            return list
        }
        private fun getItemsChild(): ArrayList<SpiritualActionStepEntity>{
            val list = arrayListOf<SpiritualActionStepEntity>()
            for (i in 1..10){
                list += SpiritualActionStepEntity(
                    i,
                    "Name",
                    false,
                    TimeCommitmentEntity(
                        TimeCommitmentType.MINUTE,
                        15
                    ),
                    1582800063344,
                    RepeatType.DAY,
                    SpiritualActionStepEntity.RepeatDayEntity(
                        10
                    ),
                    null,
                    null
                )
            }
            return list
        }
    }

    inner class SourceFactory : DataSource.Factory<Int, GoalEntity>() {
        override fun create(): DataSource<Int, GoalEntity> {
            return Source()
        }
    }
}