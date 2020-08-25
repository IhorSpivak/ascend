package com.doneit.ascend.presentation.main.create_group.master_mind.group

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.MotionEvent
import android.view.View
import android.widget.AdapterView
import android.widget.Spinner
import android.widget.SpinnerAdapter
import androidx.core.content.FileProvider
import androidx.core.view.GestureDetectorCompat
import androidx.lifecycle.Observer
import com.androidisland.ezpermission.EzPermission
import com.doneit.ascend.domain.entity.group.GroupEntity
import com.doneit.ascend.presentation.common.DefaultGestureDetectorListener
import com.doneit.ascend.presentation.dialog.ChooseImageBottomDialog
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.base.BaseFragment
import com.doneit.ascend.presentation.main.common.gone
import com.doneit.ascend.presentation.main.common.visible
import com.doneit.ascend.presentation.main.create_group.CreateGroupHostContract
import com.doneit.ascend.presentation.main.create_group.create_support_group.common.DurationAdapter
import com.doneit.ascend.presentation.main.create_group.master_mind.common.Duration
import com.doneit.ascend.presentation.main.create_group.master_mind.common.InvitedMembersAdapter
import com.doneit.ascend.presentation.main.databinding.FragmentCreateGroupBinding
import com.doneit.ascend.presentation.utils.*
import com.doneit.ascend.presentation.utils.extensions.hideKeyboard
import com.doneit.ascend.presentation.utils.extensions.toTimeStampFormat
import com.doneit.ascend.presentation.views.PriceKeyboard
import kotlinx.android.synthetic.main.fragment_create_group.*
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
import java.util.*

class CreateGroupFragment : BaseFragment<FragmentCreateGroupBinding>() {

    override val viewModelModule = Kodein.Module(this::class.java.simpleName) {
        bind<CreateGroupContract.ViewModel>() with provider {
            instance<CreateGroupHostContract.ViewModel>()
        }
    }

    private var currentPhotoPath: String? = null
    override val viewModel: CreateGroupContract.ViewModel by instance()


    private val durationAdapter by lazy {
        DurationAdapter(
            Duration.values().map { it.label }.toTypedArray()
        )
    }

    private val mDetector by lazy {
        GestureDetectorCompat(context, object : DefaultGestureDetectorListener() {
            override fun onSingleTapUp(p0: MotionEvent?): Boolean {
                return true
            }
        })
    }

    private fun initSpinner(
        spinner: Spinner,
        listener: AdapterView.OnItemSelectedListener,
        spinnerAdapter: SpinnerAdapter
    ) {
        spinner.adapter = spinnerAdapter
        spinner.onItemSelectedListener = listener

        spinner.setOnTouchListener { _, motionEvent ->
            if (mDetector.onTouchEvent(motionEvent)) {
                hideKeyboard()
            }
            false
        }
    }

    private val durationListener: AdapterView.OnItemSelectedListener by lazy {
        object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                if (p2 > 0) {
                    viewModel.createGroupModel.duration.observableField.set(Duration.values()[p2].time.toString())
                    binding.durationTitle.visible()
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }
    }

    private val membersAdapter: InvitedMembersAdapter by lazy {
        InvitedMembersAdapter {
            val removedIndex = viewModel.removeMember(it)
            if (removedIndex != -1) {
                membersAdapter.remove(removedIndex)
                if (membersAdapter.itemCount < 50) {
                    add_member_container.visibility = View.VISIBLE
                }
            }
        }
    }

    override fun viewCreated(savedInstanceState: Bundle?) {
        GroupAction.values().forEach {
            if (arguments?.containsKey(it.toString()) == true) {
                val group = requireArguments().getParcelable<GroupEntity>(it.toString())
                group?.let { currentGroup ->
                    val what = it.toString()
                    viewModel.loadParticipants(currentGroup.id, what)
                }
            }
        }
        binding.apply {
            binding.model = viewModel
            adapter = adapter
            recyclerViewAddedMembers.adapter = membersAdapter
            scroll.setOnFocusChangeListener { _, b ->
                if (b) {
                    hideKeyboard()
                }
            }
            initSpinner(durationPicker, durationListener, durationAdapter)

            viewModel.createGroupModel.duration.observableField.get()?.run {
                if (this.isNotEmpty()) durationPicker.setSelection(Duration.fromDuration(this.toInt()).ordinal)
            }
            applyMultilineFilter(description)


            mainContainer.setOnFocusChangeListener { v, b ->
                if (b) {
                    hideKeyboard()
                }
            }

            chooseSchedule.multilineEditText.setOnClickListener {
                mainContainer.requestFocus()
                viewModel.chooseScheduleTouch()
            }

            startDate.editText.setOnClickListener {
                binding.mainContainer.requestFocus()
                viewModel.chooseStartDateTouch()
            }

            numberOfMeetings.editText.setOnClickListener {
                binding.mainContainer.requestFocus()
                viewModel.chooseMeetingCountTouch(null, null)
            }

            price.editTextView.let {
                var text = it.text.toString()
                keyboardLayout.setBackground(R.color.master_mind_color)
                keyboardLayout.onButtonsClick = object : PriceKeyboard.OnButtonsClick {
                    override fun onDoneClick() {
                        text = it.text.toString()
                        viewModel.okPriceClick(it.text.toString())
                        keyboardLayout.gone()
                    }

                    override fun onCancelClick() {
                        viewModel.okPriceClick(text)
                        keyboardLayout.gone()
                    }

                }
                it.setOnFocusChangeListener { _, b ->
                    if (b) {
                        scroll.scrollTo(0, numberOfMeetings.top)
                        text = it.text.toString()
                        hideKeyboard()
                        keyboardLayout.editor = it
                        keyboardLayout.visible()
                    } else {
                        viewModel.okPriceClick(text)
                        keyboardLayout.gone()
                    }
                }
                it.setOnClickListener {
                    scroll.scrollTo(0, numberOfMeetings.top)
                    keyboardLayout.visible()
                }
            }

            placeholderDash.setOnClickListener {
                hideKeyboard()
                createImageBottomDialog().show(childFragmentManager, null)
            }

            icEdit.setOnClickListener {
                hideKeyboard()
                createImageBottomDialog().show(childFragmentManager, null)
            }





            addMemberContainer.setOnClickListener {
                viewModel.addMember(viewModel.createGroupModel.groupType!!)
            }
        }

        viewModel.changeGroup.observe(this, Observer {
            binding.apply {
                userName.text = it.name.orEmpty()
                description.text = it.description.orEmpty()
            }
        })

        viewModel.networkErrorMessage.observe(this) {
            it?.let { errorMessageIt ->
                this.showErrorDialog("Error", errorMessageIt, "", isAutoClose = true)
            }
        }

        viewModel.members.observe(this, Observer {
            if (it.size > 49) {
                add_member_container.visibility = View.VISIBLE
            }
            membersAdapter.submitList(it)
        })

        viewModel.clearReservationSeat.observe(this) {
            it?.let {
                hideKeyboard()
                mainContainer.requestFocus()
            }
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
                if (granted.contains(Manifest.permission.READ_EXTERNAL_STORAGE) && granted.contains(
                        Manifest.permission.CAMERA
                    )
                ) {
                    val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    activity!!.getExternalFilesDir(Environment.DIRECTORY_PICTURES)?.let {
                        File.createTempFile(
                            "JPEG_${Date().toTimeStampFormat()}_",
                            ".jpg",
                            it /* directory */
                        ).apply {
                            currentPhotoPath = absolutePath
                        }.also { file ->
                            FileProvider.getUriForFile(
                                context!!,
                                "com.doneit.ascend.fileprovider",
                                file
                            )?.also {
                                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, it)
                                startActivityForResult(
                                    cameraIntent,
                                    PHOTO_REQUEST_CODE
                                )
                            }
                        }
                    }
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
                viewModel.createGroupModel.image.observableField.set(
                    activity!!.checkImage(sourcePath)
                )
            }
        }
    }

    private fun createImageBottomDialog(): ChooseImageBottomDialog {
        return ChooseImageBottomDialog.create(
            arrayOf(
                ChooseImageBottomDialog.AllowedIntents.CAMERA,
                ChooseImageBottomDialog.AllowedIntents.IMAGE
            ),
            { takeAPhoto() },
            { selectFromGallery() }
        )
    }

    override fun onDestroyView() {
        recycler_view_added_members.adapter = null
        super.onDestroyView()
    }

    companion object {
        private const val GALLERY_REQUEST_CODE = 42
        private const val PHOTO_REQUEST_CODE = 43
    }
}