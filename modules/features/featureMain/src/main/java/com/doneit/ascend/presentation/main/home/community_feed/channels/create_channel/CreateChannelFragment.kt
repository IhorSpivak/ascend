package com.doneit.ascend.presentation.main.home.community_feed.channels.create_channel

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.androidisland.ezpermission.EzPermission
import com.doneit.ascend.domain.entity.chats.ChatEntity
import com.doneit.ascend.presentation.dialog.ChooseImageBottomDialog
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.base.BaseFragment
import com.doneit.ascend.presentation.main.base.CommonViewModelFactory
import com.doneit.ascend.presentation.main.databinding.FragmentNewChannelBinding
import com.doneit.ascend.presentation.utils.copyCompressed
import com.doneit.ascend.presentation.utils.createCropPhotoUri
import com.doneit.ascend.presentation.utils.extensions.requestPermissions
import com.doneit.ascend.presentation.utils.getCompressedImagePath
import com.yalantis.ucrop.UCrop
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.kodein.di.Kodein
import org.kodein.di.direct
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton
import java.io.File
import java.util.*

class CreateChannelFragment : BaseFragment<FragmentNewChannelBinding>() {
    override val viewModelModule = Kodein.Module(this::class.java.simpleName) {
        bind<ViewModelProvider.Factory>(tag = CreateChannelFragment::class.java.simpleName) with singleton {
            CommonViewModelFactory(
                kodein.direct
            )
        }

        bind<NewChannelLocalRouter>() with provider {
            NewChannelLocalRouter(
                this@CreateChannelFragment,
                instance()
            )
        }
        bind<ViewModel>(tag = CreateChannelViewModel::class.java.simpleName) with provider {
            CreateChannelViewModel(
                instance(),
                instance(),
                instance(),
                instance(),
                instance()
            )
        }

        bind<CreateChannelContract.ViewModel>() with provider {
            vm<CreateChannelViewModel>(
                instance(tag = CreateChannelFragment::class.java.simpleName)
            )
        }
    }
    override val viewModel: CreateChannelContract.ViewModel by instance()
    private var lastFileUri = Uri.EMPTY
    private val channel: ChatEntity? by lazy {
        requireArguments().getParcelable<ChatEntity>(KEY_CHANNEL)
    }

    private val cropPhotoUri by lazy { requireContext().createCropPhotoUri() }
    private val compressedPhotoPath by lazy { requireContext().getCompressedImagePath() }

    override fun viewCreated(savedInstanceState: Bundle?) {
        channel?.let {
            viewModel.setEditMode(it)
            viewModel.newChannelModel.title.observableField.set(it.title)
            viewModel.newChannelModel.description.observableField.set(it.description)
            viewModel.newChannelModel.isPrivate.set(it.isPrivate)
            viewModel.newChannelModel.image.observableField.set(it.image?.thumbnail?.url)
        }
        binding.apply {
            model = viewModel
            isInUpdateMode = channel != null
            btnAddImage.setOnClickListener {
                doIfPermissionsGranted {
                    createImageBottomDialog().show(
                        childFragmentManager,
                        ChooseImageBottomDialog::class.java.simpleName
                    )
                }
            }
            btnAdd.setOnClickListener {
                viewModel.addMembers()
            }
            isPrivate.setOnCheckedChangeListener { _, b ->
                viewModel.newChannelModel.isPrivate.set(b)
            }

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                UCrop.REQUEST_CROP -> {
                    val uri = data?.data ?: return
                    handleImageURI(source = uri)
                }
                REQUEST_CODE_GALLERY -> {
                    viewModel.onPhotoSelected(data!!.data!!, cropPhotoUri, this)
                }
                REQUEST_CODE_CAMERA -> {
                    viewModel.onPhotoSelected(lastFileUri, cropPhotoUri, this)
                }
            }
        }
    }

    private fun handleImageURI(source: Uri) {
        GlobalScope.launch {
            //todo: not working in some cases (didn't get why, android 9)
            val compressed = activity!!.copyCompressed(source, compressedPhotoPath)
            viewModel.newChannelModel.image.observableField.set(null)
            viewModel.newChannelModel.image.observableField.set(compressed)
        }
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

    private fun getCameraIntent(): Intent {
        return Intent(MediaStore.ACTION_IMAGE_CAPTURE).apply {
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

    private fun createImageBottomDialog(): ChooseImageBottomDialog {
        return ChooseImageBottomDialog.create(
            arrayOf(
                ChooseImageBottomDialog.AllowedIntents.CAMERA,
                ChooseImageBottomDialog.AllowedIntents.IMAGE
            ),
            { startActivityForResult(getCameraIntent(), REQUEST_CODE_CAMERA) },
            { startActivityForResult(getMediaIntent(), REQUEST_CODE_GALLERY) }
        )
    }

    fun getContainerId() = R.id.new_chat_container

    companion object {
        private const val IMAGE_FILENAME = "temp_image"

        private const val KEY_CHANNEL = "CHANNEL"
        private const val REQUEST_CODE_CAMERA = 101
        private const val REQUEST_CODE_GALLERY = 102
        private const val MIME_TYPE_IMAGE = "image/*"
        fun newInstance(channel: ChatEntity? = null) = CreateChannelFragment().also {
            it.arguments = Bundle().apply {
                putParcelable(KEY_CHANNEL, channel ?: return@apply)
            }
        }
    }
}