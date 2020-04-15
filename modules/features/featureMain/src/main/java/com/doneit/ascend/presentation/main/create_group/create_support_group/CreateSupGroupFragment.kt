package com.doneit.ascend.presentation.main.create_group.create_support_group

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.MotionEvent
import android.view.View
import android.widget.AdapterView
import android.widget.Spinner
import android.widget.SpinnerAdapter
import androidx.appcompat.app.AlertDialog
import androidx.core.content.FileProvider
import androidx.core.view.GestureDetectorCompat
import androidx.lifecycle.Observer
import com.androidisland.ezpermission.EzPermission
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.doneit.ascend.domain.entity.MonthEntity
import com.doneit.ascend.domain.entity.group.GroupEntity
import com.doneit.ascend.presentation.common.DefaultGestureDetectorListener
import com.doneit.ascend.presentation.dialog.ChooseImageBottomDialog
import com.doneit.ascend.presentation.dialog.ChooseImageDialog
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.base.argumented.ArgumentedFragment
import com.doneit.ascend.presentation.main.create_group.CreateGroupArgs
import com.doneit.ascend.presentation.main.create_group.CreateGroupHostContract
import com.doneit.ascend.presentation.main.create_group.common.ParticipantAdapter
import com.doneit.ascend.presentation.main.create_group.create_support_group.common.MeetingFormatsAdapter
import com.doneit.ascend.presentation.main.create_group.master_mind.common.InvitedMembersAdapter
import com.doneit.ascend.presentation.main.databinding.FragmentCreateSupportGroupBinding
import com.doneit.ascend.presentation.utils.*
import com.doneit.ascend.presentation.utils.extensions.*
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
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class CreateSupGroupFragment : ArgumentedFragment<FragmentCreateSupportGroupBinding, CreateGroupArgs>() {

    override val viewModelModule = Kodein.Module(this::class.java.simpleName) {
        bind<CreateSupGroupContract.ViewModel>() with provider {
            instance<CreateGroupHostContract.ViewModel>()
        }
    }

    override val viewModel: CreateSupGroupContract.ViewModel by instance()

    private var group: GroupEntity? = null
    private var tempUri: Uri? = null
    private var what: String? = null
    private var currentPhotoPath: String? = null

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

    private val tagsAdapter by lazy {
        MeetingFormatsAdapter(
            context!!.resources.getStringArray(
                R.array.tags_array
            )
        )
    }

    private val membersAdapter: InvitedMembersAdapter by lazy {
        InvitedMembersAdapter{
            viewModel.removeMember(it)
        }
    }
    private val mDetector by lazy {
        GestureDetectorCompat(context, object : DefaultGestureDetectorListener() {
            override fun onSingleTapUp(p0: MotionEvent?): Boolean {
                return true
            }
        })
    }

    override fun viewCreated(savedInstanceState: Bundle?) {
        GroupAction.values().forEach {
            if (arguments!!.containsKey(it.toString())){
                group = arguments!!.getParcelable(it.toString())
                if (group != null){
                    what = it.toString()
                }
            }
        }
        binding.apply {
            model = viewModel
            adapter = adapter

            actionTitle = if (what == null){
                getString(R.string.create_create)
            }else{
                what!!.capitalize()
            }

            recyclerViewAddedMembers.adapter = membersAdapter

            numberOfMeetings.editText.setOnClickListener {
                mainContainer.requestFocus()
                viewModel.chooseMeetingCountTouch(group, null)
            }

            isPrivate.setOnCheckedChangeListener { compoundButton, b ->
                viewModel.createGroupModel.isPrivate.set(b)
            }

            placeholderDash.setOnClickListener {
                hideKeyboard()
                createImageBottomDialog().show(childFragmentManager, null)
            }

            icEdit.setOnClickListener {
                hideKeyboard()
                createImageBottomDialog().show(childFragmentManager, null)
            }

            chooseSchedule.multilineEditText.setOnClickListener {
                mainContainer.requestFocus()
                viewModel.chooseScheduleTouch()
            }

            startDate.editText.setOnClickListener {
                mainContainer.requestFocus()
                viewModel.chooseStartDateTouch()
            }
        }
        viewModel.members.observe(this, Observer {
            viewModel.createGroupModel.participants.set(it.map {
                it.email!!
            })
            membersAdapter.submitList(it)
        })

        viewModel.clearReservationSeat.observe(this) {
            it?.let {
                hideKeyboard()
                mainContainer.requestFocus()
            }
        }

        initSpinner(binding.meetingsPicker, meetingFormatListener, meetingTypesAdapter)
        initSpinner(binding.tagsPicker, tagsListener, tagsAdapter)
        if (group != null){
            binding.btnComplete.apply {
                text = getString(R.string.btn_save_action)
                setOnClickListener { viewModel.updateGroup(group!!) }
            }
            viewModel.createGroupModel.isPrivate.set(group!!.isPrivate)
            viewModel.createGroupModel.apply {
                when(what){
                    GroupAction.DUPLICATE.toString() ->{name.observableField.set(group!!.name.plus("(2)"))}
                    GroupAction.EDIT.toString() ->{name.observableField.set(group!!.name)}
                }
                isPrivate.set(group!!.isPrivate)
                tags = group!!.tag!!.id
                binding.tagsPicker.setSelection(tags)
                meetingFormat.observableField.set(group!!.meetingFormat!!)
                binding.meetingsPicker.setSelection(resources.getStringArray(R.array.meeting_formats).indexOf(group!!.meetingFormat!!))
                numberOfMeetings.observableField.set(group!!.meetingsCount.toString())
                price.observableField.set(group!!.price.toString())
                description.observableField.set(group!!.description)
                val date = group!!.startTime
                year = date!!.toYear()
                month = MonthEntity.values()[date!!.toMonth()]
                day = date!!.toDayOfMonth()
                hours = date!!.toCalendar().get(Calendar.HOUR).toTimeString()
                hoursOfDay = date!!.toCalendar().get(Calendar.HOUR_OF_DAY).toTimeString()
                minutes = date!!.toCalendar().get(Calendar.MINUTE).toTimeString()
                timeType = date!!.toCalendar().get(Calendar.AM_PM).toAmPm()
                groupType = com.doneit.ascend.presentation.models.GroupType.values()[group!!.groupType!!.ordinal]
                meetingFormat.observableField.set(group!!.meetingFormat?: "")
                startDate.observableField.set(SimpleDateFormat("dd MMMM yyyy").format(date))
                selectedDays.addAll(group!!.daysOfWeek!!)
                viewModel.changeSchedule()
                Glide.with(context!!)
                    .asBitmap()
                    .load(group!!.image!!.url)
                    .into(object : SimpleTarget<Bitmap>(){
                        override fun onResourceReady(
                            resource: Bitmap,
                            transition: Transition<in Bitmap>?
                        ) {
                            image.observableField.set(context?.copyToStorage(resource))
                        }

                    })
            }
            viewModel.apply{
                members.postValue(group!!.attendees?.toMutableList())
                selectedMembers.addAll(group!!.attendees?: emptyList())
            }
        }else{
            binding.btnComplete.setOnClickListener {
                viewModel.completeClick()
            }
        }
    }

    private fun initSpinner(spinner: Spinner, listener: AdapterView.OnItemSelectedListener, spinnerAdapter: SpinnerAdapter) {
        spinner.adapter = spinnerAdapter
        spinner.onItemSelectedListener = listener

        spinner.setOnTouchListener { view, motionEvent ->
            if (mDetector.onTouchEvent(motionEvent)) {
                hideKeyboard()
            }
            false
        }
    }

    private fun selectFromGallery() {
        hideKeyboard()
        EzPermission.with(context!!)
            .permissions(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
            )
            .request { granted, _, _ ->
                if (granted.contains(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    val galleryIntent =
                        Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    galleryIntent.type = "image/*"
                    startActivityForResult(galleryIntent, GALLERY_REQUEST_CODE)
                }
            }
    }

    private fun takeAPhoto() {
        hideKeyboard()
        EzPermission.with(context!!)
            .permissions(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
            )
            .request { granted, _, _ ->
                if (granted.contains(Manifest.permission.READ_EXTERNAL_STORAGE) && granted.contains(Manifest.permission.CAMERA)) {
                    val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
                    activity!!.getExternalFilesDir(Environment.DIRECTORY_PICTURES)?.let {
                        File.createTempFile(
                            "JPEG_${timeStamp}_",
                            ".jpg",
                            it /* directory */
                        ).apply {
                            currentPhotoPath = absolutePath
                        }.also {file ->
                            FileProvider.getUriForFile(context!!, "com.doneit.ascend.fileprovider", file)?.also {
                                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, it)
                                startActivityForResult(cameraIntent, PHOTO_REQUEST_CODE)
                            }
                        }
                    }
                    /*val content = ContentValues().apply {
                        put(
                            MediaStore.Images.Media.TITLE,
                            "JPEG_${SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())}.jpg"
                        )
                        put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
                        put(MediaStore.Images.Media.DESCRIPTION, "group_image")
                    }
                    tempUri = activity?.contentResolver?.insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        content
                    )
                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, tempUri)
                    startActivityForResult(cameraIntent, PHOTO_REQUEST_CODE)*/
                }
            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK)
            when (requestCode) {
                GALLERY_REQUEST_CODE -> {
                    handleImageURI(data?.data!!)
                }
                PHOTO_REQUEST_CODE -> {
                    currentPhotoPath?.let {
                        handlePhoto(it)
                    }
                }
            }
    }

    private fun handleImageURI(sourcePath: Uri) {
        GlobalScope.launch {
            launch(Dispatchers.Main) {
                viewModel.createGroupModel.image.observableField.set(null)//in order to force observers notification
                viewModel.createGroupModel.image.observableField.set(
                    activity!!.getImagePath(
                        sourcePath
                    ).let {
                        if (it.isEmpty()) {
                            context!!.copyToStorage(sourcePath)
                        } else {
                            it
                        }
                    })
            }
        }
    }

    private fun handlePhoto(sourcePath: String) {
        GlobalScope.launch {
            launch(Dispatchers.Main) {
                viewModel.createGroupModel.image.observableField.set(null)//in order to force observers notification
                viewModel.createGroupModel.image.observableField.set(sourcePath)
            }
        }
    }

    private val meetingFormatListener: AdapterView.OnItemSelectedListener by lazy {
        object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                if(p2 > 0) {
                    viewModel.createGroupModel.meetingFormat.observableField.set(binding.meetingsPicker.selectedItem as String)
                }
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }
    }

    private val tagsListener: AdapterView.OnItemSelectedListener by lazy {
        object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                if(p2 > 0) {
                    viewModel.createGroupModel.tags = p2
                    binding.tagHint.visibility = View.VISIBLE
                }
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }
    }

    private fun createImageBottomDialog(): ChooseImageBottomDialog {
        return ChooseImageBottomDialog.create(
            { takeAPhoto() },
            { selectFromGallery() }
        )
    }

    companion object {
        private const val GALLERY_REQUEST_CODE = 42
        private const val PHOTO_REQUEST_CODE = 43
    }
}