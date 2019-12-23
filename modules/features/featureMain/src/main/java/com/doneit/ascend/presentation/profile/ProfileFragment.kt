package com.doneit.ascend.presentation.profile

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import com.androidisland.ezpermission.EzPermission
import com.doneit.ascend.presentation.main.base.BaseFragment
import com.doneit.ascend.presentation.main.databinding.FragmentProfileBinding
import com.doneit.ascend.presentation.utils.*
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.kodein.di.generic.instance
import java.io.File

class ProfileFragment : BaseFragment<FragmentProfileBinding>() {

    override val viewModelModule = ProfileViewModelModule.get(this)
    override val viewModel: ProfileContract.ViewModel by instance()

    private val cameraPhotoUri by lazy { context!!.createCameraPhotoUri(TEMP_IMAGE_NAME) }

    override fun viewCreated(savedInstanceState: Bundle?) {
        binding.model = viewModel

        viewModel.showPhotoDialog.observe(this) {
            showChangePhotoDialog({
                // open camera
                EzPermission.with(context!!)
                    .permissions(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA
                    ).request { granted, _, _ ->

                        if (granted.contains(Manifest.permission.READ_EXTERNAL_STORAGE)) {

                            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, cameraPhotoUri)

                            startActivityForResult(
                                cameraIntent,
                                GALLERY_REQUEST_CODE
                            )
                        }
                    }
            }, {
                // open gallery
                EzPermission.with(context!!)
                    .permissions(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ).request { granted, _, _ ->

                        if (granted.contains(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                            val galleryIntent = Intent(Intent.ACTION_PICK)
                            galleryIntent.type = "image/*"

                            startActivityForResult(
                                galleryIntent,
                                GALLERY_REQUEST_CODE
                            )
                        }
                    }
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

                        val path = activity!!.uriToFilePath(selected!!)
                        handleImageURI(path)
                    } else {
                        handleImageURI(cameraPhotoUri.path!!)
                    }
                }
            }
        }
    }

    private fun handleImageURI(sourcePath: String) {
        val destinationPath =
            context!!.externalCacheDir!!.path + File.separatorChar + TEMP_IMAGE_NAME + sourcePath.getFileExtension()

        GlobalScope.launch {
            val compressed = copyCompressed(sourcePath, destinationPath)

            launch(Dispatchers.Main) {
                ruIcon.setUrl(compressed)
            }
        }
    }

    companion object {
        private const val GALLERY_REQUEST_CODE = 42
        private const val TEMP_IMAGE_NAME = "group_image_temp"
    }
}
