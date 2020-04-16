package com.doneit.ascend.presentation.main.create_group.master_mind.webinar.common

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.doneit.ascend.domain.entity.group.GroupEntity
import com.doneit.ascend.domain.entity.group.GroupType
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.create_group.master_mind.webinar.CreateWebinarContract
import com.doneit.ascend.presentation.main.databinding.ListItemTimeBinding
import com.doneit.ascend.presentation.models.ValidatableField
import com.doneit.ascend.presentation.utils.GroupAction
import kotlinx.android.synthetic.main.view_multiline_edit_with_error.view.*

class TimeViewHolder(
    private val binding: ListItemTimeBinding
): RecyclerView.ViewHolder(binding.root) {
    fun bind(position: Int, viewModel: CreateWebinarContract.ViewModel, group: GroupEntity?, action: GroupAction?){
        binding.apply {
            this.position = position
            model = viewModel
            remove.setOnClickListener {
                viewModel.createGroupModel.webinarSchedule.let {
                    if (it.size == 1){
                        viewModel.chooseScheduleTouch(position)
                    }else{
                        viewModel.updateListOfTimes(position, true)
                    }
                }

            }
            add.setOnClickListener {
                viewModel.updateListOfTimes(position, false)
            }
            chooseSchedule.multilineEditText.setOnClickListener {
                mainContainer.requestFocus()
                viewModel.chooseScheduleTouch(position)
            }
            group?.let {
                if ((it.pastMeetingsCount!! > 0 && action == GroupAction.EDIT) || (group.isStarting && group.participantsCount!! > 0 && action == GroupAction.EDIT)){
                    if (position == 0){
                        chooseSchedule.apply {
                            multilineEditText.setOnClickListener {}
                            multilineEditText.setTextColor(root.context.resources.getColor(R.color.light_gray_b1bf))
                        }
                        remove.setOnClickListener {}
                    }
                }
            }
        }
    }

    companion object{
        fun create(
            viewParent: ViewGroup
        ): TimeViewHolder{
            return TimeViewHolder(
                DataBindingUtil.inflate(LayoutInflater.from(viewParent.context), R.layout.list_item_time, viewParent, false)
            )
        }
    }
}