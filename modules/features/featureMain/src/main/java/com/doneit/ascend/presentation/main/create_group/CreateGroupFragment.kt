package com.doneit.ascend.presentation.main.create_group

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.androidisland.ezpermission.EzPermission
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.base.CommonViewModelFactory
import com.doneit.ascend.presentation.main.base.argumented.ArgumentedFragment
import com.doneit.ascend.presentation.main.create_group.common.ParticipantAdapter
import com.doneit.ascend.presentation.main.databinding.FragmentCreateGroupBinding
import com.doneit.ascend.presentation.main.extensions.hideKeyboard
import com.doneit.ascend.presentation.main.extensions.vmShared
import com.doneit.ascend.presentation.utils.*
import kotlinx.android.synthetic.main.fragment_create_group.*
import kotlinx.android.synthetic.main.view_edit_with_error.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.kodein.di.Kodein
import org.kodein.di.direct
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton
import java.io.File

class CreateGroupFragment : ArgumentedFragment<FragmentCreateGroupBinding, CreateGroupArgs>() {

    override val viewModelModule = Kodein.Module(this::class.java.simpleName) {
        bind<ViewModelProvider.Factory>() with singleton { CommonViewModelFactory(kodein.direct) }
        bind<ViewModel>(tag = CreateGroupViewModel::class.java.simpleName) with provider {
            CreateGroupViewModel(
                instance(),
                instance(),
                instance(),
                instance()
            )
        }
        bind<CreateGroupContract.ViewModel>() with provider {
            vmShared<CreateGroupViewModel>(
                instance()
            )
        }
    }

    private val cameraPhotoUri by lazy { context!!.createCameraPhotoUri(TEMP_IMAGE_NAME) }
    override val viewModel: CreateGroupContract.ViewModel by instance()

    private val adapter: ParticipantAdapter by lazy {
        ParticipantAdapter(mutableListOf(), viewModel)
    }

    override fun viewCreated(savedInstanceState: Bundle?) {
        binding.lifecycleOwner = this
        binding.model = viewModel
        binding.adapter = adapter

        tvTitle.text = getString(R.string.create_group)

        chooseSchedule.editText.setOnClickListener {
            mainContainer.requestFocus()
            viewModel.chooseScheduleTouch()
        }

        startDate.editText.setOnClickListener {
            mainContainer.requestFocus()
            viewModel.chooseStartDateTouch()
        }

        binding.placeholderDash.setOnClickListener {
            pickFromGallery()
        }

        binding.icEdit.setOnClickListener {
            pickFromGallery()
        }

        viewModel.networkErrorMessage.observe(this) {
            it?.let { errorMessageIt ->
                this.showErrorDialog("Error", errorMessageIt, "", isAutoClose = true)
            }
        }

        viewModel.clearReservationSeat.observe(this) {
            it?.let {
                hideKeyboard()
                mainContainer.requestFocus()
            }
        }
    }

    private fun pickFromGallery() {

        EzPermission.with(context!!)
            .permissions(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
            )
            .request { granted, _, _ ->
                if (granted.contains(Manifest.permission.READ_EXTERNAL_STORAGE)) {

                    val galleryIntent = Intent(Intent.ACTION_PICK)
                    galleryIntent.type = "image/*"

                    val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, cameraPhotoUri)


                    val chooser =
                        Intent.createChooser(galleryIntent, "Select an App to choose an Image")
                    chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf(cameraIntent))

                    startActivityForResult(chooser, GALLERY_REQUEST_CODE)
                }
            }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK)
            when (requestCode) {
                GALLERY_REQUEST_CODE -> {
                    if(data?.data != null) {
                        val selected = data.data

                        val path = activity!!.uriToFilePath(selected!!)
                        handleImageURI(path)
                    } else {
                        handleImageURI(cameraPhotoUri.path!!)
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
                viewModel.createGroupModel.image.observableField.set(compressed)
            }
        }
    }

    companion object {
        private const val GALLERY_REQUEST_CODE = 42
        private const val TEMP_IMAGE_NAME = "group_image_temp"
    }
}