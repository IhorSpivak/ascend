package com.doneit.ascend.presentation.main.create_group.master_mind.webinar.common

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.create_group.master_mind.webinar.CreateWebinarContract
import com.doneit.ascend.presentation.main.databinding.ListItemTimeBinding
import com.doneit.ascend.presentation.models.ValidatableField
import kotlinx.android.synthetic.main.view_multiline_edit_with_error.view.*

class TimeViewHolder(
    private val binding: ListItemTimeBinding
): RecyclerView.ViewHolder(binding.root) {
    fun bind(position: Int, viewModel: CreateWebinarContract.ViewModel){
        binding.apply {
            this.position = position
            model = viewModel
            remove.setOnClickListener {
                viewModel.newScheduleItem.value?.let {
                    viewModel.newScheduleItem.postValue(it.apply { removeAt(position) })
                }
            }
            add.setOnClickListener {
                viewModel.newScheduleItem.value?.let {
                    viewModel.newScheduleItem.postValue(it.apply { add(ValidatableField()) })
                }
            }
            chooseSchedule.multilineEditText.setOnClickListener {
                viewModel.chooseScheduleTouch(position)
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