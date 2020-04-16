package com.doneit.ascend.presentation.main.create_group.master_mind.webinar.common

import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.doneit.ascend.domain.entity.group.GroupEntity
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.create_group.master_mind.webinar.CreateWebinarContract
import com.doneit.ascend.presentation.main.databinding.ListItemWebinarThemeBinding
import com.doneit.ascend.presentation.models.ValidationResult
import com.doneit.ascend.presentation.utils.GroupAction
import com.doneit.ascend.presentation.utils.isThemeValid
import kotlinx.android.synthetic.main.view_multiline_edit_with_error.view.*

class ThemeViewHolder(
    private val binding: ListItemWebinarThemeBinding
): RecyclerView.ViewHolder(binding.root) {
    fun bind(position: Int, viewModel: CreateWebinarContract.ViewModel, t: Theme, group: GroupEntity?, action: GroupAction?){
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
            viewModel.createGroupModel.themesOfMeeting[position].observableField.set(t.theme)
            theme.multilineEditText.addTextChangedListener(object : TextWatcher{
                override fun afterTextChanged(p0: Editable?) {
                    viewModel.themeList[position].theme = p0.toString()
                    notifyChange()
                }

                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

            })
            remove.setOnClickListener {
                viewModel.removeTheme(position)
            }
            group?.let {
                if ((it.pastMeetingsCount!! > 0 && action == GroupAction.EDIT) || (group.isStarting && group.participantsCount!! > 0 && action == GroupAction.EDIT)){
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