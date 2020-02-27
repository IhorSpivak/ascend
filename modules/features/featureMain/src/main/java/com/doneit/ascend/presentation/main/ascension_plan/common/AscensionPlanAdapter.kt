package com.doneit.ascend.presentation.main.ascension_plan.common

import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.doneit.ascend.domain.entity.ascension.AscensionEntity
import com.doneit.ascend.domain.entity.ascension.GoalEntity
import com.doneit.ascend.domain.entity.ascension.SpiritualStepEntity

class AscensionPlanAdapter(
) : PagedListAdapter<AscensionEntity, RecyclerView.ViewHolder>(AscensionDiffCallback()) {

    init {
        setHasStableIds(true)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ViewTypes.SPIRITUAL_ACTION.ordinal -> SpiritualViewHolder.create(parent)
            ViewTypes.GOAL.ordinal -> GoalViewHolder.create(parent)
            else -> DateViewHolder.create(parent)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)

        when (holder) {
            is SpiritualViewHolder -> holder.bind(item as SpiritualStepEntity)
            is GoalViewHolder -> holder.bind(item as GoalEntity)
            is DateViewHolder -> holder.bind(item as AscensionEntity)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is SpiritualStepEntity -> ViewTypes.SPIRITUAL_ACTION.ordinal
            is GoalEntity -> ViewTypes.GOAL.ordinal
            else -> ViewTypes.DATE.ordinal
        }
    }

    override fun getItemId(position: Int): Long {
        return getItem(position)!!.id
    }

    private enum class ViewTypes {
        SPIRITUAL_ACTION,
        GOAL,
        DATE
    }
}