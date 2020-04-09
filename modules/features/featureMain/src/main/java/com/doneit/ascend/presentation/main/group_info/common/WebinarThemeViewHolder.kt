package com.doneit.ascend.presentation.main.group_info.common

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.doneit.ascend.domain.entity.group.GroupEntity
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.databinding.ListItemThemeGroupInfoBinding

class WebinarThemeViewHolder(
    private val binding: ListItemThemeGroupInfoBinding,
    private val groupEntity: GroupEntity
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(position: Int){
        binding.apply {
            group = groupEntity
            this.position = position
        }
    }
    companion object{
        fun create(
            viewParent: ViewGroup,
            groupEntity: GroupEntity
        ): WebinarThemeViewHolder{
            return WebinarThemeViewHolder(
                DataBindingUtil.inflate(LayoutInflater.from(viewParent.context), R.layout.list_item_theme_group_info, viewParent, false),
                groupEntity
            )
        }
    }
}