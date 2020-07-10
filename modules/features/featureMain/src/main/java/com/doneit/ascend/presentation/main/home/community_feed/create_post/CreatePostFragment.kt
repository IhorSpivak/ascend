package com.doneit.ascend.presentation.main.home.community_feed.create_post

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.ClipData
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import com.androidisland.ezpermission.EzPermission
import com.doneit.ascend.domain.entity.community_feed.Post
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.base.BaseFragment
import com.doneit.ascend.presentation.main.databinding.FragmentCreatePostBinding
import com.doneit.ascend.presentation.main.home.community_feed.CommunityFeedFragment
import com.doneit.ascend.presentation.main.home.community_feed.create_post.common.CreatePostAdapter
import com.doneit.ascend.presentation.utils.extensions.requestPermissions
import com.doneit.ascend.presentation.utils.showErrorDialog
import org.kodein.di.Kodein
import org.kodein.di.generic.instance


class CreatePostFragment : BaseFragment<FragmentCreatePostBinding>() {
    override val viewModel: CreatePostContract.ViewModel by instance()

    override val viewModelModule: Kodein.Module
        get() = CreatePostViewModelModule.get(this)

    private val adapter: CreatePostAdapter by lazy {
        CreatePostAdapter(viewModel::deleteItemAt)
    }

    override fun viewCreated(savedInstanceState: Bundle?) {
        binding.apply {
            viewModel = this@CreatePostFragment.viewModel
            dashRectangleBackground.setOnClickListener {
                doIfPermissionsGranted {
                    startActivityForResult(getImageIntent(), REQUEST_CODE_MEDIA)
                }
            }
            buttonComplete.setOnClickListener { this@CreatePostFragment.viewModel.createPost() }
            rvMedia.adapter = adapter
        }
        observeData()
    }

    private fun doIfPermissionsGranted(action: () -> Unit) {
        val isGranted = EzPermission.isGranted(
            requireContext(),
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
        if (!isGranted) {
            requireContext().requestPermissions(
                listOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ),
                onGranted = {
                    action()
                },
                onDenied = {
                }
            )
        } else action()
    }

    private fun observeData() {
        with(viewModel) {
            observe(attachments, adapter::submitItems)
            observe(showPopupEvent, ::showPopup)
            observe(result, ::setResult)
        }
    }

    private fun setResult(post: Post) {
        val result = Intent().apply {
            action = CommunityFeedFragment.ACTION_NEW_POST
            putExtra(RESULT, post)
        }
        requireContext().sendBroadcast(result)
    }

    private fun showPopup(text: String) {
        showErrorDialog(
            getString(R.string.error),
            errorMessage = text,
            buttonText = getString(R.string.ok)
        )
    }

    private fun getImageIntent(): Intent {
        return Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
            type = MIME_TYPE_IMAGE
            putExtra(Intent.EXTRA_MIME_TYPES, arrayOf(MIME_TYPE_IMAGE, MIME_TYPE_VIDEO))
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_CODE_MEDIA && resultCode == RESULT_OK) {
            if (data != null) {
                processClipData(data.clipData)
                viewModel.processSingleItem(
                    uri = data.data?.toString() ?: return,
                    mimeType = getMimeTypeFromUri(data.data ?: return)
                )
                adapter.submitItems(viewModel.attachments.value.orEmpty())
            }
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun processClipData(clipData: ClipData?) {
        clipData ?: return
        for (i in 0 until clipData.itemCount) {
            val item = clipData.getItemAt(i)
            viewModel.processSingleItem(
                item.uri.toString(),
                getMimeTypeFromUri(item.uri)
            )
        }
        adapter.submitItems(viewModel.attachments.value.orEmpty())
    }

    private fun getMimeTypeFromUri(uri: Uri): String {
        return requireContext()
            .contentResolver
            .getType(uri)
            .orEmpty()
    }

    companion object {
        const val RESULT = "result"
        private const val REQUEST_CODE_MEDIA = 101
        private const val MIME_TYPE_IMAGE = "image/*"
        private const val MIME_TYPE_VIDEO = "video/*"
        fun newInstance() = CreatePostFragment()
    }
}