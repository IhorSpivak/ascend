package com.doneit.ascend.presentation.main.home.community_feed.create_post

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.ClipData
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.provider.MediaStore.ACTION_IMAGE_CAPTURE
import androidx.core.content.FileProvider
import com.androidisland.ezpermission.EzPermission
import com.doneit.ascend.domain.entity.community_feed.Post
import com.doneit.ascend.presentation.dialog.ChooseImageBottomDialog
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.base.BaseFragment
import com.doneit.ascend.presentation.main.databinding.FragmentCreatePostBinding
import com.doneit.ascend.presentation.main.home.community_feed.CommunityFeedFragment
import com.doneit.ascend.presentation.main.home.community_feed.create_post.common.CreatePostAdapter
import com.doneit.ascend.presentation.utils.extensions.requestPermissions
import com.doneit.ascend.presentation.utils.showErrorDialog
import org.kodein.di.Kodein
import org.kodein.di.generic.instance
import java.io.File
import java.util.*


class CreatePostFragment : BaseFragment<FragmentCreatePostBinding>() {
    override val viewModel: CreatePostContract.ViewModel by instance()

    override val viewModelModule: Kodein.Module
        get() = CreatePostViewModelModule.get(this)

    private val adapter: CreatePostAdapter by lazy {
        CreatePostAdapter(viewModel::deleteItemAt)
    }

    private val post: Post? by lazy {
        requireArguments().getParcelable<Post>(KEY_POST)
    }

    private var lastFileUri = Uri.EMPTY

    override fun viewCreated(savedInstanceState: Bundle?) {
        post?.let {
            viewModel.setEditMode(it)
        }
        binding.apply {
            isInUpdateMode = post != null
            viewModel = this@CreatePostFragment.viewModel
            dashRectangleBackground.setOnClickListener {
                doIfPermissionsGranted {
                    ChooseImageBottomDialog.create(
                        ChooseImageBottomDialog.AllowedIntents.values(),
                        { startActivityForResult(getCameraIntent(), REQUEST_CODE_CAMERA) },
                        { startActivityForResult(getMediaIntent(), REQUEST_CODE_GALLERY) },
                        { startActivityForResult(getVideoIntent(), REQUEST_CODE_VIDEO) }
                    ).show(childFragmentManager, ChooseImageBottomDialog::class.java.simpleName)
                }
            }
            buttonComplete.setOnClickListener { this@CreatePostFragment.viewModel.createPost() }
            rvMedia.adapter = adapter
        }
        observeData()
    }

    private fun doIfPermissionsGranted(action: () -> Unit) {
        val isGrantedStorage = EzPermission.isGranted(
            requireContext(),
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
        val isGrantedCamera = EzPermission.isGranted(
            requireContext(),
            Manifest.permission.CAMERA
        )
        if (!isGrantedCamera || !isGrantedStorage) {
            requireContext().requestPermissions(
                listOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA
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

    private fun getCameraIntent(): Intent {
        return Intent(ACTION_IMAGE_CAPTURE).apply {
            lastFileUri = FileProvider.getUriForFile(
                requireContext(),
                requireContext().applicationContext.packageName + ".fileprovider",
                createImageFile()
            )
            putExtra(
                MediaStore.EXTRA_OUTPUT, lastFileUri
            )
        }
    }

    private fun createImageFile(): File {
        return File(
            requireContext()
                .getExternalFilesDir(
                    Environment.DIRECTORY_PICTURES
                ), "${IMAGE_FILENAME}${UUID.randomUUID()}.jpg"
        )
    }

    private fun getMediaIntent(): Intent {
        return Intent.createChooser(
            Intent(Intent.ACTION_GET_CONTENT).apply {
                putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
                type = MIME_TYPE_IMAGE
            }, getString(R.string.select_from_gallery)
        )
    }

    private fun getVideoIntent(): Intent {
        return Intent.createChooser(
            Intent(Intent.ACTION_GET_CONTENT).apply {
                putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
                type = MIME_TYPE_VIDEO
            }, getString(R.string.take_video)
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        fun getMimeType() = if (requestCode == REQUEST_CODE_VIDEO)
            MIME_TYPE_VIDEO
        else MIME_TYPE_IMAGE

        fun doOnData(ifSingleItem: () -> Unit, ifMultiple: () -> Unit) {
            if (data?.data != null) {
                ifSingleItem()
            } else {
                ifMultiple()
            }
        }
        if (resultCode == RESULT_OK) {
            doOnData(
                ifSingleItem = {
                    viewModel.processSingleItem(
                        data!!.data!!.toString(),
                        getMimeType()
                    )
                },
                ifMultiple = {
                    if (data?.clipData == null) {
                        viewModel.processSingleItem(
                            lastFileUri.toString(),
                            getMimeType()
                        )
                        return@doOnData
                    }
                    processClipData(data.clipData)
                }
            )
            adapter.submitItems(viewModel.attachments.value.orEmpty())
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
        private const val IMAGE_FILENAME = "temp_image"
        private const val KEY_POST = "POST"
        private const val REQUEST_CODE_CAMERA = 101
        private const val REQUEST_CODE_VIDEO = 102
        private const val REQUEST_CODE_GALLERY = 103
        private const val MIME_TYPE_IMAGE = "image/*"
        private const val MIME_TYPE_VIDEO = "video/*"
        fun newInstance(post: Post? = null) = CreatePostFragment().also {
            it.arguments = Bundle().apply {
                putParcelable(KEY_POST, post ?: return@apply)
            }
        }
    }
}