package com.doneit.ascend.presentation.main.create_group.master_mind.individual

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.view.MotionEvent
import android.view.View
import android.widget.AdapterView
import android.widget.EditText
import androidx.core.view.GestureDetectorCompat
import androidx.core.view.get
import androidx.lifecycle.Observer
import com.androidisland.ezpermission.EzPermission
import com.doneit.ascend.presentation.common.DefaultGestureDetectorListener
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.base.BaseFragment
import com.doneit.ascend.presentation.main.create_group.CreateGroupHostContract
import com.doneit.ascend.presentation.main.create_group.create_support_group.common.MeetingFormatsAdapter
import com.doneit.ascend.presentation.main.create_group.master_mind.common.InvitedMembersAdapter
import com.doneit.ascend.presentation.main.databinding.FragmentCreateIndividualGroupBinding
import com.doneit.ascend.presentation.utils.*
import com.doneit.ascend.presentation.utils.extensions.hideKeyboard
import com.redmadrobot.inputmask.MaskedTextChangedListener
import kotlinx.android.synthetic.main.view_edit_with_error.view.*
import kotlinx.android.synthetic.main.view_multiline_edit_with_error.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class IndividualGroupFragment : BaseFragment<FragmentCreateIndividualGroupBinding>() {

    override val viewModelModule = Kodein.Module(this::class.java.simpleName) {
        bind<IndividualGroupContract.ViewModel>() with provider {
            instance<CreateGroupHostContract.ViewModel>()
        }
    }

    private val membersAdapter: InvitedMembersAdapter by lazy {
        InvitedMembersAdapter {
            viewModel.removeMember(it)
        }
    }

    /*private val durationAdapter by lazy {
        MeetingFormatsAdapter(
            context!!.resources.getStringArray(
                R.array.duration_array
            )
        )
    }
    private val detector by lazy {
        GestureDetectorCompat(context, object : DefaultGestureDetectorListener() {
            override fun onSingleTapUp(p0: MotionEvent?): Boolean {
                return true
            }
        })
    }*/

    private val compressedPhotoPath by lazy { context!!.getCompressedImagePath() }
    private val tempPhotoUri by lazy { context!!.createTempPhotoUri() }
    override val viewModel: IndividualGroupContract.ViewModel by instance()

    override fun viewCreated(savedInstanceState: Bundle?) {

        binding.apply {
            model = viewModel
            recyclerViewAddedMembers.adapter = membersAdapter
            chooseSchedule.multilineEditText.setOnClickListener {
                mainContainer.requestFocus()
                viewModel.chooseScheduleTouch()
            }

            startDate.editText.setOnClickListener {
                mainContainer.requestFocus()
                viewModel.chooseStartDateTouch()
            }

            numberOfMeetings.editText.setOnClickListener {
                mainContainer.requestFocus()
                viewModel.chooseMeetingCountTouch()
            }

            duration.isClickable = false

            dashRectangleBackground.setOnClickListener {
                pickFromGallery()
            }
            icEdit.setOnClickListener {
                pickFromGallery()
            }
            price.editText.setOnClickListener {
                scroll.scrollTo(0, numberOfMeetings.top)
                viewModel.onPriceClick(price.editText)
            }
        }

        viewModel.members.observe(this, Observer {
            membersAdapter.submitList(it)
            //viewModel.participants.
        })

        viewModel.networkErrorMessage.observe(this) {
            it?.let { errorMessageIt ->
                this.showErrorDialog("Error", errorMessageIt, "", isAutoClose = true)
            }
        }

        binding.addMemberContainer.setOnClickListener {
            viewModel.addMember(viewModel.createGroupModel.groupType!!)
        }
        //initSpinner()
    }

    private fun pickFromGallery() {
        hideKeyboard()
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
                    if(data?.data != null) {
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
                binding.dashRectangleBackground.setOnClickListener {  }
                viewModel.createGroupModel.image.observableField.set(compressed)
            }
        }
    }

    /*private fun initSpinner() {
        binding.durationPicker.apply {
            adapter = durationAdapter
            onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                        if(p2 > 0) {
                            viewModel.createGroupModel.duration = p2.toString()
                            binding.durationHint.visibility = View.VISIBLE
                        }
                    }

                    override fun onNothingSelected(p0: AdapterView<*>?) {
                    }
                }

            setOnTouchListener { view, motionEvent ->
                if (detector.onTouchEvent(motionEvent)) {
                    hideKeyboard()
                }
                false
            }
        }
    }*/

    companion object {
        private const val GALLERY_REQUEST_CODE = 42
        private const val PRICE_MASK = "[0999]{.}[09]"
    }
}