package com.doneit.ascend.presentation.main.create_group.master_mind.webinar

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import com.androidisland.ezpermission.EzPermission
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.doneit.ascend.domain.entity.group.GroupEntity
import com.doneit.ascend.presentation.dialog.ChooseImageBottomDialog
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.base.argumented.ArgumentedFragment
import com.doneit.ascend.presentation.main.create_group.CreateGroupArgs
import com.doneit.ascend.presentation.main.create_group.CreateGroupHostContract
import com.doneit.ascend.presentation.main.create_group.master_mind.common.InvitedMembersAdapter
import com.doneit.ascend.presentation.main.create_group.master_mind.webinar.common.ThemeAdapter
import com.doneit.ascend.presentation.main.create_group.master_mind.webinar.common.TimeAdapter
import com.doneit.ascend.presentation.main.databinding.FragmentCreateWebinarBinding
import com.doneit.ascend.presentation.models.ValidationResult
import com.doneit.ascend.presentation.utils.GroupAction
import com.doneit.ascend.presentation.utils.copyToStorage
import com.doneit.ascend.presentation.utils.extensions.hideKeyboard
import com.doneit.ascend.presentation.utils.getImagePath
import com.doneit.ascend.presentation.utils.isWebinarDescriptionValid
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

class CreateWebinarFragment : ArgumentedFragment<FragmentCreateWebinarBinding, CreateGroupArgs>(){
    override val viewModelModule = Kodein.Module(this::class.java.simpleName) {
        bind<CreateWebinarContract.ViewModel>() with provider {
            instance<CreateGroupHostContract.ViewModel>()
        }
    }

    override val viewModel: CreateWebinarContract.ViewModel by instance()

    private var group: GroupEntity? = null
    private var tempUri: Uri? = null
    private var what: GroupAction? = null

    private val membersAdapter: InvitedMembersAdapter by lazy {
        InvitedMembersAdapter {
            viewModel.removeMember(it)
        }
    }

    private val themeAdapter: ThemeAdapter by lazy {
        ThemeAdapter(viewModel, group, what)
    }

    private val timeAdapter: TimeAdapter by lazy {
        TimeAdapter(viewModel, group, what)
    }

    override fun viewCreated(savedInstanceState: Bundle?) {
        GroupAction.values().forEach {
            if (arguments!!.containsKey(it.toString())) {
                group = arguments!!.getParcelable(it.toString())
                if (group != null) {
                    what = it
                }
            }
        }

        viewModel.createGroupModel.description.validator = { s ->
            ValidationResult().apply {
                if (s.isWebinarDescriptionValid().not()) {
                    isSucceed = false
                    errors.add(R.string.error_description_webinar)
                }
            }
        }

        binding.apply {
            model = viewModel
            actionTitle = if (what == null){
                getString(R.string.create_create)
            }else{
                what?.toString()?.capitalize()
            }
            recyclerViewAddedMembers.adapter = membersAdapter
            webinarThemes.apply{
                adapter = themeAdapter
            }
            timeSchedule.adapter = timeAdapter

            startDate.editText.setOnClickListener {
                viewModel.onSelectStartDate()
            }
            dashRectangleBackground.setOnClickListener {
                hideKeyboard()
                createImageBottomDialog().show(childFragmentManager, null)
            }

            icEdit.setOnClickListener {
                hideKeyboard()
                createImageBottomDialog().show(childFragmentManager, null)
            }
            numberOfMeetings.editText.setOnClickListener {
                viewModel.chooseMeetingCountTouch(group, what)
            }
            addMemberContainer.setOnClickListener {
                viewModel.addMember(viewModel.createGroupModel.groupType!!)
            }
            description.multilineEditText.setOnEditorActionListener(object : TextView.OnEditorActionListener{
                override fun onEditorAction(p0: TextView?, p1: Int, p2: KeyEvent?): Boolean {
                    if (p1 == EditorInfo.IME_ACTION_DONE) {
                        hideKeyboard()
                        return true
                    }
                    return false
                }
            })
            scrollableContainer.setOnFocusChangeListener { v, b ->
                if (b) {
                    hideKeyboard()
                }
            }
        }
        viewModel.members.observe(this, androidx.lifecycle.Observer {
            viewModel.createGroupModel.participants.set(it.map {
                it.email!!
            })
            membersAdapter.submitList(it)
        })

        viewModel.newScheduleItem.observe(this, androidx.lifecycle.Observer {
            timeAdapter.data = it
        })

        viewModel.themes.observe(this, androidx.lifecycle.Observer {
            themeAdapter.data = it
        })
        if (group != null){
            binding.buttonComplete.apply {
                text = getString(R.string.btn_save_action)
                setOnClickListener {
                    when (what) {
                        GroupAction.DUPLICATE -> viewModel.completeClick()
                        GroupAction.EDIT -> viewModel.updateGroup(group!!)
                    }
                }
            }
            viewModel.updateFields(group!!, what!!.toString())
            if (group!!.pastMeetingsCount!! > 0 && what == GroupAction.EDIT){
                binding.apply {
                    startDate.editText.setOnClickListener {  }
                    startDate.setColor(resources.getColor(R.color.light_gray_b1bf))
                }
            }
            Glide.with(context!!)
                .asBitmap()
                .load(group!!.image!!.url)
                .into(object : SimpleTarget<Bitmap>(){
                    override fun onResourceReady(
                        resource: Bitmap,
                        transition: Transition<in Bitmap>?
                    ) {
                        viewModel.createGroupModel.image.observableField.set(context?.copyToStorage(resource))
                    }

                })
        }else{
            binding.buttonComplete.setOnClickListener {
                viewModel.completeClick()
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
                if (granted.contains(Manifest.permission.READ_EXTERNAL_STORAGE) && granted.contains(Manifest.permission.CAMERA)) {
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