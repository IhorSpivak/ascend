package com.doneit.ascend.presentation.main.home.community_feed.preview

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.viewpager.widget.ViewPager
import com.doneit.ascend.domain.entity.community_feed.Attachment
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.base.BaseActivity
import com.doneit.ascend.presentation.main.databinding.ActivityPreviewBinding
import com.doneit.ascend.presentation.main.home.community_feed.preview.common.PreviewAdapter
import kotlinx.android.synthetic.main.pager_item_video.view.*
import org.kodein.di.Kodein

class PreviewActivity : BaseActivity() {

    override fun diModule() = Kodein.Module(this::class.java.simpleName) {
    }

    private val attachments by lazy {
        requireNotNull(intent.extras).getParcelableArrayList<Attachment>(KEY_ATTACHMENTS)!!
    }

    private val selectedItem by lazy {
        requireNotNull(intent.extras).getInt(KEY_SELECTED_ITEM, 0) + 1
    }

    private lateinit var binding: ActivityPreviewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_preview)
        binding.apply {
            vpMedia.adapter = PreviewAdapter(attachments)
            vpMedia.offscreenPageLimit = OFFSCREEN_PAGE_LIMIT
            vpMedia.setCurrentItem(selectedItem - 1, false)
            vpMedia.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {

                private var previousPos = vpMedia.currentItem

                override fun onPageScrollStateChanged(state: Int) {
                }

                override fun onPageScrolled(
                    position: Int,
                    positionOffset: Float,
                    positionOffsetPixels: Int
                ) {
                }

                override fun onPageSelected(position: Int) {
                    tvCounter.text = getString(
                        R.string.format_quantity,
                        position + 1,
                        attachments.size
                    )
                    val child = vpMedia.getChildAt(previousPos)
                    previousPos = position
                    if (child?.pvPlayer != null) {
                        child.pvPlayer.player?.playWhenReady = false
                    }
                }
            })
            ivBack.setOnClickListener { onBackPressed() }
            tvCounter.text = getString(R.string.format_quantity, selectedItem, attachments.size)
        }
    }

    override fun onPause() {
        with(binding) {
            val child = vpMedia.getChildAt(vpMedia.currentItem)
            if (child.pvPlayer != null) {
                child.pvPlayer.player?.playWhenReady = false
            }
        }
        super.onPause()
    }

    companion object {

        internal const val OFFSCREEN_PAGE_LIMIT = 3
        internal const val KEY_ATTACHMENTS = "key_attachments"
        internal const val KEY_SELECTED_ITEM = "key_selected_item"

        fun createIntent(context: Context, attachments: Collection<Attachment>, selectedItem: Int) =
            Intent(context, PreviewActivity::class.java).apply {
                putExtra(KEY_ATTACHMENTS, ArrayList(attachments))
                putExtra(KEY_SELECTED_ITEM, selectedItem)
            }
    }
}