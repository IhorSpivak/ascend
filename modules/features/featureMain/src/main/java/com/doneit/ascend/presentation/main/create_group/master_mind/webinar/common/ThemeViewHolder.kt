package com.doneit.ascend.presentation.main.create_group.master_mind.webinar.common

import android.text.InputType
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.doneit.ascend.domain.entity.group.GroupEntity
import com.doneit.ascend.domain.entity.group.GroupType
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.create_group.master_mind.webinar.CreateWebinarContract
import com.doneit.ascend.presentation.main.databinding.ListItemWebinarThemeBinding
import com.doneit.ascend.presentation.models.ValidatableField
import com.doneit.ascend.presentation.models.ValidationResult
import com.doneit.ascend.presentation.utils.GroupAction
import com.doneit.ascend.presentation.utils.isThemeValid
import kotlinx.android.synthetic.main.view_multiline_edit_with_error.view.*

class ThemeViewHolder(
    private val binding: ListItemWebinarThemeBinding
): RecyclerView.ViewHolder(binding.root) {
    fun bind(position: Int, viewModel: CreateWebinarContract.ViewModel, field: ValidatableField, group: GroupEntity?, action: GroupAction?){
        binding.apply {
            this.position = position
            model = viewModel
            field.validator = { s ->
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
            group?.let {
                if ((it.pastMeetingsCount!! > 0 && action == GroupAction.EDIT) || (group.isStarting && group.participantsCount!! > 0)){
                    if (position == it.pastMeetingsCount!! - 1){
                        theme.multilineEditText.apply {
                            inputType = InputType.TYPE_NULL
                            isFocusable = false
                            setTextColor(root.context.resources.getColor(R.color.light_gray_b1bf))
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
        ): ThemeViewHolder{
            return ThemeViewHolder(
                DataBindingUtil.inflate(LayoutInflater.from(viewParent.context), R.layout.list_item_webinar_theme, viewParent, false)
            )
        }
    }
}