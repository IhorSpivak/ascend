package com.doneit.ascend.presentation.profile.regular_user.community.common

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.doneit.ascend.presentation.models.PresentationCommunityModel

class CommunityAdapter(
    private val items: List<PresentationCommunityModel>,
    private val selectionListener: (String) -> Unit
) : RecyclerView.Adapter<CommunityViewHolder>() {

    private var lastSelectedIndex = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommunityViewHolder {
        return CommunityViewHolder.create(parent)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: CommunityViewHolder, position: Int) {
        holder.bind(items[position]) {
            if (lastSelectedIndex != -1) {
                items[lastSelectedIndex].isSelected = false
                notifyItemChanged(lastSelectedIndex)
            }

            lastSelectedIndex = position
            items[position].isSelected = true
            selectionListener.invoke(items[position].title)
        }
    }

    fun getSelectedItem(): PresentationCommunityModel? {
        return items.firstOrNull { it.isSelected }
    }
}