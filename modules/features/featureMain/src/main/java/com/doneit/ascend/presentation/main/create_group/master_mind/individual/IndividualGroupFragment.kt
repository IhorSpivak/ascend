package com.doneit.ascend.presentation.main.create_group.master_mind.individual

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.androidisland.ezpermission.EzPermission
import com.doneit.ascend.presentation.dialog.ChooseImageBottomDialog
import com.doneit.ascend.presentation.main.base.BaseFragment
import com.doneit.ascend.presentation.main.create_group.CreateGroupHostContract
import com.doneit.ascend.presentation.main.create_group.CreateGroupHostFragment
import com.doneit.ascend.presentation.main.create_group.master_mind.common.InvitedMembersAdapter
import com.doneit.ascend.presentation.main.databinding.FragmentCreateIndividualGroupBinding
import com.doneit.ascend.presentation.utils.*
import com.doneit.ascend.presentation.utils.extensions.hideKeyboard
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

    private val compressedPhotoPath by lazy { context!!.getCompressedImagePath() }
    private val tempPhotoUri by lazy { context!!.createTempPhotoUri() }
    private var tempUri: Uri? = null
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
                viewModel.chooseMeetingCountTouch(null, null)
            }

            duration.isClickable = false

            dashRectangleBackground.setOnClickListener {
                hideKeyboard()
                createImageBottomDialog().show(childFragmentManager, null)
            }

            icEdit.setOnClickListener {
                hideKeyboard()
                createImageBottomDialog().show(childFragmentManager, null)
            }
            price.editText.apply {
                setOnFocusChangeListener { view, b ->
                    if (b){
                        scroll.scrollTo(0, numberOfMeetings.top)
                        viewModel.onPriceClick(price.editText)
                    }
                }
                price.editText.setOnClickListener {
                    scroll.scrollTo(0, numberOfMeetings.top)
                    viewModel.onPriceClick(price.editText)
                }
            }
            scroll.setOnFocusChangeListener { v, b ->
                if (b) {
                    hideKeyboard()
                }
            }
        }

        viewModel.members.observe(this, Observer {
            membersAdapter.submitList(it)
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

    /*override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            CreateGroupHostFragment.REQUEST_PERMISSION -> {
                if (grantResults.isNotEmpty() &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED&&
                    grantResults[1] == PackageManager.PERMISSION_GRANTED&&
                    grantResults[2] == PackageManager.PERMISSION_GRANTED) {
                    pickFromGallery()
                }
            }
            else -> {
                super.onRequestPermissionsResult(requestCode, permissions, grantResults)
            }
        }
    }
    private fun takeImageIfHasPermissions(){
        context?.let {
            if(it.hasPermissions()){
                pickFromGallery()
            }else{
                requestPermissions(CreateGroupHostFragment.PERMISSIONS, CreateGroupHostFragment.REQUEST_PERMISSION)
            }
        }
    }*/

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

    private fun pickFromGallery() {
        hideKeyboard()
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        galleryIntent.type = "image/*"

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


        val chooser =
            Intent.createChooser(galleryIntent, "Select an App to choose an Image")
        chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf(cameraIntent))

        startActivityForResult(chooser, GALLERY_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                GALLERY_REQUEST_CODE -> {
                    handleImageURI(data?.data!!)
                }
                PHOTO_REQUEST_CODE -> {
                    handleImageURI(tempUri!!)
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

    private fun createImageBottomDialog(): ChooseImageBottomDialog {
        return ChooseImageBottomDialog.create(
            { takeAPhoto() },
            { selectFromGallery() }
        )
    }

    companion object {
        private const val GALLERY_REQUEST_CODE = 42
        private const val PHOTO_REQUEST_CODE = 43
        private fun Context.hasPermissions(): Boolean{
            return (PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(this, CreateGroupHostFragment.PERMISSIONS[0]) &&
                    PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(this, CreateGroupHostFragment.PERMISSIONS[1]) &&
                    PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(this, CreateGroupHostFragment.PERMISSIONS[2]))
        }
    }
}