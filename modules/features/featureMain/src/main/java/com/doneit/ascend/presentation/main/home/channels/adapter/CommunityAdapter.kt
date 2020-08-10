package com.doneit.ascend.presentation.main.home.channels.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.doneit.ascend.domain.entity.user.Community
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.databinding.ListItemCommunitySelectBinding

class CommunityAdapter(
    private val onCommunitySelect: (Community) -> Unit = {}
): RecyclerView.Adapter<CommunityAdapter.ViewHolder>() {

    private val communityList = ArrayList<Community>()
    private var selectedPosition = 0

    private var recyclerView: RecyclerView? = null

    fun setData(communities: List<Community>) {
        communityList.clear()
        communityList.addAll(communities)
        notifyDataSetChanged()
    }

    private fun scrollSelectedPosition() {
        recyclerView?.post {
            recyclerView?.smoothScrollToPosition(selectedPosition)
        }
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        this.recyclerView = recyclerView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(
            DataBindingUtil.inflate<ListItemCommunitySelectBinding>(
                LayoutInflater.from(parent.context),
                R.layout.list_item_community_select,
                parent,
                false
            ).root
        )

    override fun getItemCount() = communityList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(communityList[position])

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val binding = DataBindingUtil.getBinding<ListItemCommunitySelectBinding>(itemView)

        init {
            binding?.apply {
                name.setOnClickListener {
                    name.isSelected = !name.isSelected

                    val previousSelectedPosition = selectedPosition
                    selectedPosition = adapterPosition

                    onCommunitySelect(communityList[adapterPosition])

                    notifyItemChanged(previousSelectedPosition, Any())

                    scrollSelectedPosition()
                }
            }
        }

        fun bind(community: Community) {
            binding?.apply {
                this.position = adapterPosition
                this.lastPosition = itemCount - 1
                this.community = community

                name.isSelected = selectedPosition == adapterPosition
            }
        }

    }

}