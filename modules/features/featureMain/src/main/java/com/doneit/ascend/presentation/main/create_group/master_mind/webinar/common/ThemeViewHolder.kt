package com.doneit.ascend.presentation.main.create_group.master_mind.webinar.common

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.create_group.master_mind.webinar.CreateWebinarContract
import com.doneit.ascend.presentation.main.databinding.ListItemWebinarThemeBinding
import com.doneit.ascend.presentation.models.ValidationResult
import com.doneit.ascend.presentation.utils.isThemeValid

class ThemeViewHolder(
    private val binding: ListItemWebinarThemeBinding
): RecyclerView.ViewHolder(binding.root) {
    fun bind(position: Int, viewModel: CreateWebinarContract.ViewModel){
        binding.apply {
            this.position = position
            model = viewModel
            viewModel.createGroupModel.themesOfMeeting[position].validator = { s ->
                val result = ValidationResult()

                if (s.isThemeValid().not()) {
                    result.isSucceed = false
                    result.errors.add(R.string.error_theme)
                }

                result
            }
            remove.setOnClickListener {
                viewModel.themes.value?.let {
                    viewModel.themes.postValue(it.apply { removeAt(position) })
                    viewModel.createGroupModel.numberOfMeetings.observableField.set(it.size.toString())
                }
            }
        }
    }

    companion object{
        fun create(
            viewParent: ViewGroup
        ): ThemeViewHolder{
            return ThemeViewHolder(
                DataBindingUtil.inflate(LayoutInflater.from(viewParent.context), R.layout.list_item_webinar_theme, viewParent, false)
            )
        }
    }
}