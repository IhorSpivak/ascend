package com.doneit.ascend.presentation.profile.regular_user

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import com.androidisland.ezpermission.EzPermission
import com.doneit.ascend.presentation.dialog.EditFieldDialog
import com.doneit.ascend.presentation.dialog.EditFieldDialogOptions
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.base.BaseFragment
import com.doneit.ascend.presentation.main.databinding.FragmentProfileUserBinding
import com.doneit.ascend.presentation.main.extensions.vmShared
import com.doneit.ascend.presentation.models.PresentationMessage
import com.doneit.ascend.presentation.profile.common.ProfileViewModel
import com.doneit.ascend.presentation.profile.master_mind.MMProfileFragment.Companion.TEMP_CROP_IMAGE__NAME
import com.doneit.ascend.presentation.utils.*
import com.doneit.ascend.presentation.utils.extensions.sendEmail
import com.yalantis.ucrop.UCrop
import kotlinx.android.synthetic.main.fragment_profile_user.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import java.io.File



class UserProfileFragment : BaseFragment<FragmentProfileUserBinding>() {

    override val viewModelModule = Kodein.Module(this::class.java.simpleName) {
        bind<UserProfileContract.ViewModel>() with provider { vmShared<ProfileViewModel>(instance()) }
    }

    override val viewModel: UserProfileContract.ViewModel by instance()

    private val cameraPhotoUri by lazy { context!!.createCameraPhotoUri(TEMP_IMAGE_NAME) }
    private val cropPhotoUri by lazy { context!!.createCropPhotoUri(TEMP_CROP_IMAGE__NAME) }

    override fun viewCreated(savedInstanceState: Bundle?) {
        binding.model = viewModel

        viewModel.fetchData()
        viewModel.showPhotoDialog.observe(this) {
            showPhotoDialog()
        }

        fullName.setOnClickListener {
            EditFieldDialog.create(requireContext(), EditFieldDialogOptions(
                R.string.edit_full_name,
                R.string.error_full_name,
                R.string.hint_enter_full_name,
                viewModel.user.value?.fullName ?: ""
            ) {
                viewModel.updateFullName(it)
            }).show()
        }

        mm_followed.setOnClickListener {
            viewModel.onMMFollowedClick()
        }

        changePhone.setOnClickListener {
            viewModel.onChangePhoneClick()
        }

        location.setOnClickListener {
            viewModel.onLocationClick()
        }

        changePassword.setOnClickListener {
            viewModel.onChangePasswordClick()
        }

        editEmail.setOnClickListener {
            viewModel.onEditEmailClick()
        }

        notSettings.setOnClickListener {
            viewModel.onNotificationSettingsClick()
        }

        paymentPortal.setOnClickListener {
            viewModel.user.value?.let {
                viewModel.onPaymentsClick(it.isMasterMind)
            }
        }

        contactSupport.setOnClickListener {
            activity!!.sendEmail(Constants.SUPPORT_EMAIL)
        }
    }

    private fun showPhotoDialog() {
        showChangePhotoDialog({
            EzPermission.with(context!!)
                .permissions(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA
                )
                .request { granted, _, _ ->
                    if (granted.containsAll(
                            listOf(
                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.CAMERA
                            )
                        )
                    ) {

                        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, cameraPhotoUri)
                        startActivityForResult(
                            cameraIntent,
                            CAMERA_REQUEST_CODE
                        )
                    }
                }
        }, {
            EzPermission.with(context!!)
                .permissions(
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

                    if (granted.contains(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                        val galleryIntent = Intent(Intent.ACTION_PICK)
                        galleryIntent.type = "image/*"

                        startActivityForResult(
                            galleryIntent,
                            GALLERY_REQUEST_CODE
                        )
                    }
                }
        }, {
            viewModel.updateProfileIcon(null)
        }, viewModel.showDeleteButton.value ?: false)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                GALLERY_REQUEST_CODE -> {
                    if (data?.data != null) {
                        val galleryPhotoUri = data.data!!
                        viewModel.onAvatarSelected(galleryPhotoUri, cropPhotoUri, this)
                    }
                }
                CAMERA_REQUEST_CODE -> {
                    viewModel.onAvatarSelected(cameraPhotoUri, cropPhotoUri, this)
                }
                UCrop.REQUEST_CROP -> {
                    val uri = data?.data ?: return
                    handleImageURI(uri)
                }
            }
        }
    }

    private fun handleImageURI(source: Uri) {
        val destinationPath =
            context!!.externalCacheDir!!.path + File.separatorChar + TEMP_IMAGE_NAME

        GlobalScope.launch {
            val compressed = activity!!.copyCompressed(source, destinationPath)

            viewModel.updateProfileIcon(compressed)
        }
    }

    companion object {
        private const val GALLERY_REQUEST_CODE = 42
        private const val CAMERA_REQUEST_CODE = 41
        private const val TEMP_IMAGE_NAME = "profile_image_temp.jpg"
    }
}
