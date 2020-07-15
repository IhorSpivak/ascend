package com.doneit.ascend.presentation.main.home.community_feed.share_post

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.FrameLayout
import androidx.core.widget.doAfterTextChanged
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.doneit.ascend.domain.entity.user.UserEntity
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.common.gone
import com.doneit.ascend.presentation.main.databinding.FragmentSharePostBinding
import com.doneit.ascend.presentation.main.home.community_feed.share_post.common.ShareChatAdapter
import com.doneit.ascend.presentation.main.home.community_feed.share_post.common.ShareUserAdapter
import com.doneit.ascend.presentation.models.community_feed.SharePostFilter
import com.doneit.ascend.presentation.utils.extensions.visible
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton


class SharePostBottomSheetFragment : BottomSheetDialogFragment(), KodeinAware {

    override val kodein: Kodein = Kodein.lazy {
        extend(_parentKodein, true)
        import(viewModelModule, true)
    }
    private val _parentKodein: Kodein by closestKodein()
    private val viewModelModule = Kodein.Module(this::class.java.simpleName) {
        bind<SharePostContract.ViewModel>() with singleton {
            SharePostViewModel(
                instance(),
                instance(),
                instance(),
                instance(tag = "postId"),
                instance(tag= "userId")
            )
        }

        bind<Long>(tag = "postId") with provider {
            requireArguments().getLong(KEY_POST_ID)
        }

        bind<Long>(tag = "userId") with provider {
            requireArguments().getParcelable<UserEntity>(KEY_USER)!!.id
        }
    }

    private lateinit var binding: FragmentSharePostBinding
    private val viewModel: SharePostContract.ViewModel by instance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_share_post,
            container,
            false
        )
        dialog!!.setOnShowListener { dialog ->
            val d = dialog as BottomSheetDialog
            val bottomSheetInternal =
                d.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            bottomSheetInternal?.layoutParams?.height = ViewGroup.LayoutParams.MATCH_PARENT
            BottomSheetBehavior.from<View?>(bottomSheetInternal!!).state =
                BottomSheetBehavior.STATE_EXPANDED
        }
        return binding.root
    }

    private val chatAdapter: ShareChatAdapter by lazy {
        ShareChatAdapter {
            viewModel.shareChat(it.id)
        }
    }

    private val userAdapter: ShareUserAdapter by lazy {
        ShareUserAdapter {
            viewModel.shareToUser(it)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //TODO: refactor if possible, for opening view on full screen
        view.viewTreeObserver.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                view.viewTreeObserver.removeOnGlobalLayoutListener(this)

                val dialog = dialog as BottomSheetDialog
                val bottomSheet =
                    dialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as FrameLayout?
                val behavior = BottomSheetBehavior.from(bottomSheet!!)
                behavior.state = BottomSheetBehavior.STATE_EXPANDED

                val newHeight = activity?.window?.decorView?.measuredHeight
                val viewGroupLayoutParams = bottomSheet.layoutParams
                viewGroupLayoutParams.height = newHeight ?: 0
                bottomSheet.layoutParams = viewGroupLayoutParams
            }
        })
        viewModel.chats.observe(viewLifecycleOwner, Observer {
            chatAdapter.submitList(it)
        })
        viewModel.channels.observe(viewLifecycleOwner, Observer {
            chatAdapter.submitList(it)
        })
        viewModel.users.observe(viewLifecycleOwner, Observer {
            userAdapter.submitList(it)
        })
        viewModel.sharePostFilter.observe(viewLifecycleOwner, Observer {
            viewModel.updateSearch(viewModel.filterTextAll.value!!)
        })
        binding.apply {
            model = viewModel
            rgFilter.setOnCheckedChangeListener { radioGroup, i ->
                when (i) {
                    R.id.radio0 -> {
                        viewModel.sharePostFilter.postValue(SharePostFilter.CHAT)
                        rvShareTo.adapter = chatAdapter
                    }
                    R.id.radio1 -> {
                        viewModel.sharePostFilter.postValue(SharePostFilter.CHANNEL)
                        rvShareTo.adapter = chatAdapter
                    }
                    R.id.radio2 -> {
                        viewModel.sharePostFilter.postValue(SharePostFilter.USER)
                        rvShareTo.adapter = userAdapter
                    }
                }
            }

            lifecycleOwner = this@SharePostBottomSheetFragment
            clearSearch.setOnClickListener {
                tvSearch.text.clear()
                clearSearch.gone()
            }
            tvSearch.doAfterTextChanged {
                viewModel.filterTextAll.postValue(it.toString())
                viewModel.updateSearch(it.toString())
                clearSearch.visible(it.isNullOrEmpty().not())
            }
            rvShareTo.adapter = chatAdapter
        }

    }


    companion object {
        private const val KEY_POST_ID = "KEY_POST_ID"
        private const val KEY_USER = "KEY_USER"
        fun newInstance(postId: Long, user: UserEntity) = SharePostBottomSheetFragment().apply {
            arguments = Bundle().apply {
                putLong(KEY_POST_ID, postId)
                putParcelable(KEY_USER, user)
            }
        }
    }
}