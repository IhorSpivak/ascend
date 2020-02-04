package com.doneit.ascend.presentation.main.create_group.create_support_group

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.MotionEvent
import android.view.View
import android.widget.AdapterView
import androidx.core.view.GestureDetectorCompat
import com.androidisland.ezpermission.EzPermission
import com.doneit.ascend.presentation.common.DefaultGestureDetectorListener
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.base.argumented.ArgumentedFragment
import com.doneit.ascend.presentation.main.create_group.common.ParticipantAdapter
import com.doneit.ascend.presentation.main.create_group.create_support_group.common.MeetingFormatsAdapter
import com.doneit.ascend.presentation.main.create_group.master_mind.CreateGroupArgs
import com.doneit.ascend.presentation.main.create_group.master_mind.CreateGroupViewModel
import com.doneit.ascend.presentation.main.databinding.FragmentCreateSupportGroupBinding
import com.doneit.ascend.presentation.utils.copyCompressed
import com.doneit.ascend.presentation.utils.copyFile
import com.doneit.ascend.presentation.utils.createTempPhotoUri
import com.doneit.ascend.presentation.utils.extensions.hideKeyboard
import com.doneit.ascend.presentation.utils.extensions.vmShared
import com.doneit.ascend.presentation.utils.getCompressedImagePath
import kotlinx.android.synthetic.main.fragment_create_support_group.*
import kotlinx.android.synthetic.main.view_edit_with_error.view.*
import kotlinx.android.synthetic.main.view_multiline_edit_with_error.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider

class CreateSupGroupFragment : ArgumentedFragment<FragmentCreateSupportGroupBinding, CreateGroupArgs>() {

    override val viewModelModule = Kodein.Module(this::class.java.simpleName) {

        bind<CreateSupGroupContract.ViewModel>() with provider {
            vmShared<CreateGroupViewModel>(
                instance()
            )
        }
    }

    override val viewModel: CreateSupGroupContract.ViewModel by instance()

    private val compressedPhotoPath by lazy { context!!.getCompressedImagePath() }
    private val tempPhotoUri by lazy { context!!.createTempPhotoUri() }

    private val adapter: ParticipantAdapter by lazy {
        ParticipantAdapter(mutableListOf(), viewModel)
    }
    private val meetingTypesAdapter by lazy {
        MeetingFormatsAdapter(
            context!!.resources.getStringArray(
                R.array.meeting_formats
            )
        )
    }
    private val mDetector by lazy {
        GestureDetectorCompat(context, object : DefaultGestureDetectorListener() {
            override fun onSingleTapUp(p0: MotionEvent?): Boolean {
                return true
            }
        })
    }

    override fun viewCreated(savedInstanceState: Bundle?) {
        binding.model = viewModel
        binding.adapter = adapter

        chooseSchedule.multilineEditText.setOnClickListener {
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

        viewModel.clearReservationSeat.observe(this) {
            it?.let {
                hideKeyboard()
                mainContainer.requestFocus()
            }
        }

        initSpinner()
    }

    private fun initSpinner() {
        binding.meetingsPicker.adapter = meetingTypesAdapter
        binding.meetingsPicker.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    if(p2 > 0) {
                        val format = binding.meetingsPicker.selectedItem as String
                        viewModel.createGroupModel.meetingFormat.observableField.set(format)
                    }
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                }
            }

        binding.meetingsPicker.setOnTouchListener { view, motionEvent ->
            if (mDetector.onTouchEvent(motionEvent)) {
                hideKeyboard()
            }
            false
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
                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, tempPhotoUri)

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
                    if (data?.data != null) {
                        val galleryPhotoUri = context!!.copyFile(data.data!!, tempPhotoUri)
                        handleImageURI(galleryPhotoUri)
                    } else {
                        handleImageURI(tempPhotoUri)
                    }
                }
            }
    }

    private fun handleImageURI(sourcePath: Uri) {
        GlobalScope.launch {
            val compressed = activity!!.copyCompressed(sourcePath, compressedPhotoPath)

            launch(Dispatchers.Main) {
                viewModel.createGroupModel.image.observableField.set(null)//in order to force observers notification
                viewModel.createGroupModel.image.observableField.set(compressed)
            }
        }
    }

    companion object {
        private const val GALLERY_REQUEST_CODE = 42
    }
}