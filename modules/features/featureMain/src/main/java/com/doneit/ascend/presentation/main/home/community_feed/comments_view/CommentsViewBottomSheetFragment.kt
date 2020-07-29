package com.doneit.ascend.presentation.main.home.community_feed.comments_view

import android.app.Activity.RESULT_OK
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnPreDraw
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.doneit.ascend.domain.entity.community_feed.Comment
import com.doneit.ascend.domain.entity.user.UserEntity
import com.doneit.ascend.domain.use_case.PagedList
import com.doneit.ascend.presentation.common.RvLazyAdapter
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.databinding.FragmentCommentsViewBinding
import com.doneit.ascend.presentation.main.home.community_feed.comments_view.common.CommentsAdapter
import com.doneit.ascend.presentation.main.home.community_feed.comments_view.common.CommentsClickListener
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


class CommentsViewBottomSheetFragment : BottomSheetDialogFragment(), KodeinAware {

    override val kodein: Kodein = Kodein.lazy {
        extend(_parentKodein, true)
        import(viewModelModule, true)
    }
    private val _parentKodein: Kodein by closestKodein()
    private val viewModelModule = Kodein.Module(this::class.java.simpleName) {
        bind<CommentsViewContract.ViewModel>() with singleton {
            CommentsViewViewModel(instance(), instance(tag = "postId"), instance())
        }

        bind<Long>(tag = "postId") with provider {
            postId
        }
    }
    private lateinit var binding: FragmentCommentsViewBinding
    private val viewModel: CommentsViewContract.ViewModel by instance()
    private val postId by lazy {
        requireArguments().getLong(KEY_POST_ID)
    }
    private val commentsAdapter by RvLazyAdapter {
        CommentsAdapter(
            requireArguments().getParcelable(KEY_USER)!!,
            commentsClickListener(),
            doAfterListUpdated = ::scrollToStart
        ) to { binding.rvComments }
    }

    private fun commentsClickListener(): CommentsClickListener {
        return CommentsClickListener(
            onUserClick = {
                viewModel.onUserClick(it)
            },
            onDeleteClick = {
                viewModel.onDeleteComment(it)
            }
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.AppBottomSheetDialogTheme)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_comments_view,
            container,
            false
        )
        return binding.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState).apply {
            setOnShowListener {
                val d = this as BottomSheetDialog
                BottomSheetBehavior.from(
                    d.findViewById(com.google.android.material.R.id.design_bottom_sheet)!!
                ).setState(BottomSheetBehavior.STATE_EXPANDED)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.model = viewModel
        commentsAdapter
        binding.apply {
            lifecycleOwner = this@CommentsViewBottomSheetFragment
            rvComments.itemAnimator = null
            send.setOnClickListener {
                if (message.text.toString().isNotBlank()) {
                    viewModel.leaveComment(message.text.toString())
                    message.text.clear()
                }

            }
        }
        observeData()
    }

    private fun scrollToStart() {
        if (isResumed) {
            with(binding.rvComments.layoutManager as LinearLayoutManager) {
                binding.rvComments.doOnPreDraw {
                    scrollToPosition(0)
                }
            }
        }
    }

    private fun onGetComments(comments: PagedList<Comment>) {
        viewModel.setCommentsCount(comments.firstOrNull()?.postCommentsCount ?: 0)
        commentsAdapter.submitList(comments)
    }

    private fun observeData() {
        with(viewModel) {
            observe(comments, ::onGetComments)
        }
    }

    private fun <T> observe(ld: LiveData<T>, handler: (T) -> Unit) {
        ld.observe(viewLifecycleOwner::getLifecycle) {
            handler(it)
        }
    }

    override fun onDestroy() {
        requireParentFragment()
            .onActivityResult(REQUEST_CODE_COMMENTS, RESULT_OK, Intent().apply {
                putExtra(KEY_POST_ID, postId)
                putExtra(KEY_COMMENTS_COUNT, viewModel.commentsCount.value ?: 0)
            })
        super.onDestroy()
    }

    companion object {
        const val REQUEST_CODE_COMMENTS = 1233
        const val KEY_COMMENTS_COUNT = "KEY_COMMENTS_COUNT"
        const val KEY_POST_ID = "KEY_POST_ID"
        const val KEY_USER = "KEY_USER"
        fun newInstance(postId: Long, user: UserEntity) = CommentsViewBottomSheetFragment().apply {
            arguments = Bundle().apply {
                putLong(KEY_POST_ID, postId)
                putParcelable(KEY_USER, user)
            }
        }
    }
}