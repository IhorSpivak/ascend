package com.doneit.ascend.presentation.main.group_list.common

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.doneit.ascend.domain.entity.GroupEntity
import com.doneit.ascend.domain.entity.UserEntity
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.base.LifecycleViewHolder
import com.doneit.ascend.presentation.main.databinding.TemplateHorGroupItemBinding
import java.text.SimpleDateFormat
import java.util.*

class GroupHorViewHolder(
    private val binding: TemplateHorGroupItemBinding
) : LifecycleViewHolder(binding.root) {

    fun bind(item: GroupEntity, user: UserEntity?) {
        binding.item = item

        try {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val startDate: Date = dateFormat.parse(item.startTime)
            val currentDate = Calendar.getInstance().time

            if (startDate.before(currentDate) && user != null) {
                // show

                if (user.role == "master_mind") {
                    binding.showJoinButton = false
                    binding.showStartButton = true
                } else {
                    binding.showJoinButton = true
                    binding.showStartButton = false
                }

            } else {
                // hide
                binding.showJoinButton = false
                binding.showStartButton = false
            }
        } catch (e: Exception) {
            e.printStackTrace()

            binding.showJoinButton = false
            binding.showStartButton = false
        }

        binding.executePendingBindings()
    }

    companion object {
        fun create(parent: ViewGroup): GroupHorViewHolder {
            val binding: TemplateHorGroupItemBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.template_hor_group_item,
                parent,
                false
            )

            return GroupHorViewHolder(binding)
        }
    }
}