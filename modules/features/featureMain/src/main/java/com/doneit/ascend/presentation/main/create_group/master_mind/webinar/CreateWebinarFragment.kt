package com.doneit.ascend.presentation.main.create_group.master_mind.webinar

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.recyclerview.widget.LinearLayoutManager
import com.androidisland.ezpermission.EzPermission
import com.doneit.ascend.domain.entity.group.GroupEntity
import com.doneit.ascend.presentation.dialog.ChooseImageBottomDialog
import com.doneit.ascend.presentation.main.base.argumented.ArgumentedFragment
import com.doneit.ascend.presentation.main.create_group.CreateGroupArgs
import com.doneit.ascend.presentation.main.create_group.CreateGroupHostContract
import com.doneit.ascend.presentation.main.create_group.master_mind.common.InvitedMembersAdapter
import com.doneit.ascend.presentation.main.create_group.master_mind.webinar.common.ThemeAdapter
import com.doneit.ascend.presentation.main.create_group.master_mind.webinar.common.TimeAdapter
import com.doneit.ascend.presentation.main.databinding.FragmentCreateWebinarBinding
import com.doneit.ascend.presentation.models.ValidatableField
import com.doneit.ascend.presentation.utils.GroupAction
import com.doneit.ascend.presentation.utils.copyToStorage
import com.doneit.ascend.presentation.utils.extensions.hideKeyboard
import com.doneit.ascend.presentation.utils.getImagePath
import kotlinx.android.synthetic.main.view_edit_with_error.view.*
import kotlinx.android.synthetic.main.view_multiline_edit_with_error.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class CreateWebinarFragment : ArgumentedFragment<FragmentCreateWebinarBinding, CreateGroupArgs>(){
    override val viewModelModule = Kodein.Module(this::class.java.simpleName) {
        bind<CreateWebinarContract.ViewModel>() with provider {
            instance<CreateGroupHostContract.ViewModel>()
        }
    }

    override val viewModel: CreateWebinarContract.ViewModel by instance()

    private var group: GroupEntity? = null
    private var tempUri: Uri? = null
    private var what: String? = null

    private val membersAdapter: InvitedMembersAdapter by lazy {
        InvitedMembersAdapter {
            viewModel.removeMember(it)
        }
    }

    private val themeAdapter: ThemeAdapter by lazy {
        ThemeAdapter(viewModel)
    }

    private val timeAdapter: TimeAdapter by lazy {
        TimeAdapter(viewModel)
    }

    override fun viewCreated(savedInstanceState: Bundle?) {
        GroupAction.values().forEach {
            if (arguments!!.containsKey(it.toString())) {
                group = arguments!!.getParcelable(it.toString())
                if (group != null) {
                    what = it.toString()
                }
            }
        }

        binding.apply {
            model = viewModel
            recyclerViewAddedMembers.adapter = membersAdapter
            webinarThemes.apply{
                adapter = themeAdapter
            }
            timeSchedule.adapter = timeAdapter
            buttonComplete.setOnClickListener {
                viewModel.completeClick()
            }
            startDate.editText.setOnClickListener {
                viewModel.chooseStartDateTouch()
            }
            chooseSchedule.multilineEditText.setOnClickListener {
                viewModel.chooseScheduleTouch()
            }
            remove.setOnClickListener {
                viewModel.newScheduleItem.value?.let {
                    if (it.size == 0){
                        viewModel.chooseScheduleTouch()
                    }else{
                        viewModel.newScheduleItem.postValue(it.apply { removeAt(0) })
                    }
                }
            }
            add.setOnClickListener {
                viewModel.newScheduleItem.value?.let {
                    viewModel.newScheduleItem.postValue(it.apply { add(ValidatableField()) })
                }
            }

            dashRectangleBackground.setOnClickListener {
                createImageBottomDialog().show(childFragmentManager, null)
            }

            icEdit.setOnClickListener {
                createImageBottomDialog().show(childFragmentManager, null)
            }

            webinarPrice.editText.apply {
                setOnFocusChangeListener { view, b ->
                    if (b) {
                        hideKeyboard()
                        viewModel.onPriceClick(this)
                    }
                }
                setOnClickListener {
                    hideKeyboard()
                    viewModel.onPriceClick(this)
                }
            }
            numberOfMeetings.editText.setOnClickListener {
                viewModel.chooseMeetingCountTouch()
            }
            addMemberContainer.setOnClickListener {
                viewModel.addMember(viewModel.createGroupModel.groupType!!)
            }
        }
        viewModel.members.observe(this, androidx.lifecycle.Observer {
            viewModel.createGroupModel.participants.set(it.map {
                it.email!!
            })
            membersAdapter.submitList(it)
        })

        viewModel.newScheduleItem.observe(this, androidx.lifecycle.Observer {
            viewModel.createGroupModel.webinarSchedule = it
            timeAdapter.data = it
            binding.scroll.invalidate()
            binding.scroll.requestLayout()
        })

        viewModel.themes.observe(this, androidx.lifecycle.Observer {
            viewModel.createGroupModel.themesOfMeeting = it
            themeAdapter.data = it
        })
        viewModel.themesOfMeeting.observe(this, androidx.lifecycle.Observer { count ->
            viewModel.createGroupModel.themesOfMeeting.apply {
                when {
                    size == 0 -> {
                        addAll(ArrayList<ValidatableField>().apply {
                            for (i in 1..count) {
                                this.add(ValidatableField())
                            }
                        })
                    }
                    size > count -> {
                        val range = viewModel.createGroupModel.themesOfMeeting.size - count
                        viewModel.createGroupModel.themesOfMeeting = this.dropLast(range).toMutableList()
                    }
                    size < count -> {
                        addAll(ArrayList<ValidatableField>().apply {
                            val range = count - viewModel.createGroupModel.themesOfMeeting.size
                            for (i in 1..range) {
                                this.add(ValidatableField())
                            }
                        })
                    }

                }
            }
            viewModel.themes.postValue(viewModel.createGroupModel.themesOfMeeting)
            binding.scroll.invalidate()
            binding.scroll.requestLayout()
        })
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
                if (granted.contains(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    val content = ContentValues().apply {
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
                    startActivityForResult(cameraIntent, PHOTO_REQUEST_CODE)
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
                    handleImageURI(tempUri!!)
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