package com.doneit.ascend.presentation.main.home.community_feed.preview

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.doneit.ascend.domain.entity.community_feed.Attachment
import com.doneit.ascend.domain.entity.community_feed.ContentType
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.base.BaseFragment
import com.doneit.ascend.presentation.main.databinding.FragmentPreviewBinding
import com.doneit.ascend.presentation.main.home.community_feed.preview.common.PreviewAdapter
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider

class PreviewFragment : BaseFragment<FragmentPreviewBinding>() {
    override val viewModel: PreviewContract.ViewModel by instance()
    override val viewModelModule: Kodein.Module
        get() = PreviewViewModelModule.get(this)

    override val kodeinModule: Kodein.Module = Kodein.Module(this::class.java.simpleName) {
        bind(tag = KEY_ATTACHMENTS) from provider {
            attachments
        }
    }

    private val attachments: ArrayList<Attachment> by lazy {
        requireArguments().getParcelableArrayList<Attachment>(KEY_ATTACHMENTS)!!.apply {
            add(
                Attachment(
                    1,
                    ContentType.VIDEO,
                    "http://techslides.com/demos/sample-videos/small.mp4"
                )
            )
        }
    }

    private val selectedItem by lazy {
        requireArguments().getInt(KEY_SELECTED_ITEM, 0) + 1
    }

    override fun viewCreated(savedInstanceState: Bundle?) {
        binding.apply {
            vpMedia.adapter = PreviewAdapter(attachments)
            vpMedia.setCurrentItem(selectedItem - 1, false)
            vpMedia.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
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
                }
            })
            ivBack.setOnClickListener { activity?.onBackPressed() }
            tvCounter.text = getString(R.string.format_quantity, selectedItem, attachments.size)
        }
    }

    override fun onDestroyView() {
        (activity as? AppCompatActivity)?.apply {
            setSupportActionBar(null)
        }
        super.onDestroyView()
    }

    companion object {

        internal const val KEY_ATTACHMENTS = "key_attachments"
        internal const val KEY_SELECTED_ITEM = "key_selected_item"

        fun newInstance(attachments: Collection<Attachment>, selectedItem: Int) =
            PreviewFragment().apply {
                arguments = Bundle().apply {
                    putParcelableArrayList(KEY_ATTACHMENTS, ArrayList(attachments))
                    putInt(KEY_SELECTED_ITEM, selectedItem)
                }
            }
    }
}