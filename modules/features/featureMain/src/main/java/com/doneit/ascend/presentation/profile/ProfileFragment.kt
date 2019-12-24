package com.doneit.ascend.presentation.profile

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import com.androidisland.ezpermission.EzPermission
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.base.BaseFragment
import com.doneit.ascend.presentation.main.databinding.FragmentProfileBinding
import com.doneit.ascend.presentation.models.PresentationMessage
import com.doneit.ascend.presentation.utils.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.kodein.di.generic.instance
import java.io.File

class ProfileFragment : BaseFragment<FragmentProfileBinding>() {

    override val viewModelModule = ProfileViewModelModule.get(this)
    override val viewModel: ProfileContract.ViewModel by instance()

    private val cameraPhotoUri by lazy {  context!!.createCameraPhotoUri(TEMP_IMAGE_NAME) }

    override fun viewCreated(savedInstanceState: Bundle?) {
        binding.model = viewModel

        viewModel.showPhotoDialog.observe(this) {
            showChangePhotoDialog({

                EzPermission.with(context!!)
                    .permissions(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA
                    )
                    .request { granted, _, _ ->
                        if (granted.containsAll(listOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA))) {

                            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, cameraPhotoUri)
                            startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE)
                        }
                    }
            }, {
                EzPermission.with(context!!)
                    .permissions(
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ).request { granted, denied, _ ->

                        if (denied.isEmpty().not()) {
                            viewModel.errorMessage.call(
                                PresentationMessage(
                                    Messages.DEFAULT_ERROR.getId(),
                                    content = getString(R.string.gallery_denied)
                                )
                            )
                        }

                        if (granted.contains(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                            val galleryIntent = Intent(Intent.ACTION_PICK)
                            galleryIntent.type = "image/*"

                            startActivityForResult(
                                galleryIntent,
                                GALLERY_REQUEST_CODE
                            )
                        }
                    }
            },{
                viewModel.updateProfileIcon(null)
            })
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                GALLERY_REQUEST_CODE -> {
                    if (data?.data != null) {
                        val selected = data.data

                        handleImageURI(selected!!)
                    }
                }
                CAMERA_REQUEST_CODE -> {
                    handleImageURI(cameraPhotoUri)
                }
            }
        }
    }

    private fun handleImageURI(source: Uri) {
        val destinationPath =
            context!!.externalCacheDir!!.path + File.separatorChar + TEMP_IMAGE_NAME + source.path!!.getFileExtension()

        GlobalScope.launch {
            val compressed = context!!.copyCompressed(source, destinationPath)

            viewModel.updateProfileIcon(compressed)
        }
    }

    companion object {
        private const val GALLERY_REQUEST_CODE = 42
        private const val CAMERA_REQUEST_CODE = 41
        private const val TEMP_IMAGE_NAME = "profile_image_temp"
    }
}
