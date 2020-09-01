package com.doneit.ascend.presentation.main.filter.community_filter

import android.os.Bundle
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.doneit.ascend.domain.entity.user.Community
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.common.visible
import com.doneit.ascend.presentation.main.filter.base_filter.BaseFilter

abstract class CommunityFilter<T : CommunityFilterModel> : BaseFilter<T>() {

    abstract override val viewModel: CommunityFilterAbstractContract.ViewModel<T>

    private var initWithCommunity: Community? = null

    override fun viewCreated(savedInstanceState: Bundle?) {
        super.viewCreated(savedInstanceState)
        observeData()
    }

    override fun setupBinding() = with(binding) {
        super.setupBinding()
        tvCommunitiesTitle.visible()
        communitiesScroll.visible()
    }

    protected open fun observeData() = with(viewModel) {
        observe(communities, ::handleCommunities)
    }

    override fun initFilter(initFilter: T) {
        super.initFilter(initFilter)
        initWithCommunity = initFilter.community
    }

    private fun handleCommunities(communities: List<Community>) = with(binding) {
        for (community in communities) {
            radioGroup.addView(
                RadioButton(requireContext()).apply {
                    layoutParams = RadioGroup.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    ).apply {
                        width = ViewGroup.LayoutParams.WRAP_CONTENT
                        height = ViewGroup.LayoutParams.WRAP_CONTENT
                        setMargins(
                            resources.getDimensionPixelSize(R.dimen.default_margin_small),
                            resources.getDimensionPixelSize(R.dimen.default_margin_small),
                            resources.getDimensionPixelSize(R.dimen.default_margin_small),
                            resources.getDimensionPixelSize(R.dimen.default_margin_small)
                        )
                        setPadding(
                            resources.getDimensionPixelSize(R.dimen.default_margin),
                            resources.getDimensionPixelSize(R.dimen.default_margin_small),
                            resources.getDimensionPixelSize(R.dimen.default_margin),
                            resources.getDimensionPixelSize(R.dimen.default_margin_small)
                        )
                    }
                    text = getString(community.resId)
                    setTextColor(
                        ContextCompat.getColorStateList(
                            requireContext(),
                            R.color.pref_time_text_selector
                        )
                    )
                    typeface = ResourcesCompat.getFont(requireContext(), R.font.red_hat_font)
                    buttonDrawable = null
                    setBackgroundResource(R.drawable.webinar_type_filter_button)
                    setOnClickListener { viewModel.communitySelected(community) }
                }
            )
        }
        radioGroup.check(radioGroup.getChildAt(initWithCommunity?.ordinal ?: return).id)
    }
}