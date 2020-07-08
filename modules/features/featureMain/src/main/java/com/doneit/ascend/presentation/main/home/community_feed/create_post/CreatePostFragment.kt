package com.doneit.ascend.presentation.main.home.community_feed.create_post

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import com.doneit.ascend.presentation.main.base.BaseFragment
import com.doneit.ascend.presentation.main.databinding.FragmentCreatePostBinding
import org.kodein.di.Kodein
import org.kodein.di.generic.instance


class CreatePostFragment : BaseFragment<FragmentCreatePostBinding>() {
    override val viewModel: CreatePostContract.ViewModel by instance()

    override val viewModelModule: Kodein.Module
        get() = CreatePostViewModelModule.get(this)

    override fun viewCreated(savedInstanceState: Bundle?) {
        binding.apply {
            viewModel = this@CreatePostFragment.viewModel
            dashRectangleBackground.setOnClickListener {
                startActivityForResult(getImageIntent(), REQUEST_CODE_MEDIA)
            }
        }
    }

    private fun getImageIntent(): Intent {
        return Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            type = "image/*"
            putExtra(Intent.EXTRA_MIME_TYPES, arrayOf("image/*", "video/*"))
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_CODE_MEDIA && resultCode == RESULT_OK) {
            if (data != null) {

                return
            }
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    companion object {
        private const val REQUEST_CODE_MEDIA = 101

        fun newInstance() = CreatePostFragment()
    }
}