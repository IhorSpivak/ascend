package com.doneit.ascend.presentation.main.home.community_feed.comments_view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LiveData
import com.doneit.ascend.domain.entity.community_feed.Comment
import com.doneit.ascend.domain.use_case.PagedList
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.databinding.FragmentCommentsViewBinding
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
            CommentsViewViewModel(instance(), instance(tag = "postId"))
        }

        bind<Long>(tag = "postId") with provider {
            requireArguments().getLong(KEY_POST_ID)
        }
    }
    private lateinit var binding: FragmentCommentsViewBinding
    private val viewModel: CommentsViewContract.ViewModel by instance()
    private val commentsAdapter by lazy {
        //TODO
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {

        }
        observeData()
    }

    private fun onGetComments(comments: PagedList<Comment>) {

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

    companion object {
        private const val KEY_POST_ID = "KEY_POST_ID"
        fun newInstance(postId: Long) = CommentsViewBottomSheetFragment().apply {
            arguments = Bundle().apply {
                putLong(KEY_POST_ID, postId)
            }
        }
    }
}