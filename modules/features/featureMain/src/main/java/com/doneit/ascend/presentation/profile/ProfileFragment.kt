package com.doneit.ascend.presentation.profile

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import androidx.core.content.FileProvider
import com.androidisland.ezpermission.EzPermission
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.base.BaseFragment
import com.doneit.ascend.presentation.main.databinding.FragmentProfileBinding
import com.doneit.ascend.presentation.main.models.PresentationMessage
import com.doneit.ascend.presentation.utils.*
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.kodein.di.generic.instance
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class ProfileFragment : BaseFragment<FragmentProfileBinding>() {

    override val viewModelModule = ProfileViewModelModule.get(this)
    override val viewModel: ProfileContract.ViewModel by instance()

    private val cameraPhotoUri by lazy {
        //        Uri.fromFile(
//            File(
//                requireActivity().externalCacheDir?.path,
//                "fname_" + System.currentTimeMillis().toString() + ".jpg"
//            )
//        )

        //requireActivity().createCameraPhotoUri(TEMP_IMAGE_NAME)
        requireActivity().cameraPhotoUri(TEMP_IMAGE_NAME)
    }

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
                    ).request { granted, denied, _ ->

                        if (denied.contains(Manifest.permission.CAMERA)) {
                            viewModel.errorMessage.call(
                                PresentationMessage(
                                    Messages.DEFAULT_ERROR.getId(),
                                    content = getString(R.string.camera_denied)
                                )
                            )
                        }

                        if (granted.contains(Manifest.permission.CAMERA)) {

                            Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
                                // Ensure that there's a camera activity to handle the intent
                                takePictureIntent.resolveActivity(requireActivity().packageManager)
                                    ?.also {
                                        // Create the File where the photo should go
                                        val photoFile: File? = try {
                                            createImageFile()
                                        } catch (ex: IOException) {
                                            // Error occurred while creating the File
                                            null
                                        }

                                        // Continue only if the File was successfully created
                                        photoFile?.also {
                                            val photoURI: Uri = FileProvider.getUriForFile(
                                                requireContext(),
                                                "com.doneit.ascend",
                                                it
                                            )
                                            takePictureIntent.putExtra(
                                                MediaStore.EXTRA_OUTPUT,
                                                photoURI
                                            )
                                            startActivityForResult(
                                                takePictureIntent,
                                                CAMERA_REQUEST_CODE
                                            )
                                        }
                                    }
                            }

//                            Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
//                                takePictureIntent.resolveActivity(requireActivity().packageManager)
//                                    ?.also {
//
//                                        takePictureIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
//                                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, cameraPhotoUri)
//
//                                        startActivityForResult(
//                                            takePictureIntent,
//                                            CAMERA_REQUEST_CODE
//                                        )
//                                    }
//                            }
//                            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//                            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, cameraPhotoUri)
//                            startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE)
                        }
                    }
            }, {
                // open gallery
                EzPermission.with(context!!)
                    .permissions(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
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
                CAMERA_REQUEST_CODE -> {
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

    lateinit var currentPhotoPath: String

    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File =
            requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            currentPhotoPath = absolutePath
        }
    }

    companion object {
        private const val GALLERY_REQUEST_CODE = 42
        private const val CAMERA_REQUEST_CODE = 43
        private const val TEMP_IMAGE_NAME = "profile_image_temp"
    }
}
