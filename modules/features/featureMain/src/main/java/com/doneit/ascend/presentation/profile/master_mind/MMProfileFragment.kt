package com.doneit.ascend.presentation.profile.master_mind

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.appcompat.app.AlertDialog
import com.androidisland.ezpermission.EzPermission
import com.doneit.ascend.presentation.MainActivityListener
import com.doneit.ascend.presentation.dialog.DialogPattern
import com.doneit.ascend.presentation.dialog.EditFieldDialog
import com.doneit.ascend.presentation.dialog.EditFieldDialogOptions
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.base.BaseFragment
import com.doneit.ascend.presentation.main.databinding.FragmentProfileMasterMindBinding
import com.doneit.ascend.presentation.models.PresentationMessage
import com.doneit.ascend.presentation.profile.common.ProfileViewModel
import com.doneit.ascend.presentation.utils.*
import com.doneit.ascend.presentation.utils.extensions.sendEmail
import com.doneit.ascend.presentation.utils.extensions.vmShared
import com.yalantis.ucrop.UCrop
import kotlinx.android.synthetic.main.fragment_profile_master_mind.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider

class MMProfileFragment : BaseFragment<FragmentProfileMasterMindBinding>() {

    private var currentDialog: AlertDialog? = null

    override val viewModelModule = Kodein.Module(this::class.java.simpleName) {
        bind<MMProfileContract.ViewModel>() with provider { vmShared<ProfileViewModel>(instance()) }
    }

    override val viewModel: MMProfileContract.ViewModel by instance()

    private val compressedPhotoPath by lazy { requireContext().getCompressedImagePath() }
    private val tempPhotoUri by lazy { requireContext().createTempPhotoUri() }
    private val cropPhotoUri by lazy { requireContext().createCropPhotoUri() }
    var listener: MainActivityListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = (context as MainActivityListener).apply {
            setTitle(getString(R.string.profile_title))
        }
    }

    override fun onResume() {
        super.onResume()
        listener?.apply {
            setSearchEnabled(false)
            setFilterEnabled(false)
            setChatEnabled(false)
            setShareEnabled(true)
            setShareInAppEnabled(true)
            setTitle(getString(R.string.profile_title))
        }
    }

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
                R.string.enter_full_name,
                viewModel.user.value?.fullName.orEmpty()
            ) {
                viewModel.updateFullName(it)
            }).show()
        }

        displayName.setOnClickListener {
            EditFieldDialog.create(requireContext(), EditFieldDialogOptions(
                R.string.edit_display_name,
                R.string.error_display_name,
                R.string.hint_enter_display_name,
                viewModel.user.value?.displayName.orEmpty()
            ) {
                viewModel.updateDisplayName(it)
            }).show()
        }

        bio.setOnClickListener {
            viewModel.onEditBioClick()
        }

        short_description.setOnClickListener {
            EditFieldDialog.create(requireContext(), EditFieldDialogOptions(
                R.string.edit_short_description,
                R.string.error_short_description,
                R.string.hint_enter_short_description,
                viewModel.user.value?.description.orEmpty()
            ) {
                viewModel.updateShortDescription(it)
            }).show()
        }

        mm_followed.setOnClickListener {
            viewModel.onMMFollowedClick()
        }

        rating.setOnClickListener {
            viewModel.onRatingsClick()
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

        blockedUsers.setOnClickListener {
            viewModel.onBlockedUsersClick()
        }

        contactSupport.setOnClickListener {
            activity!!.sendEmail(Constants.SUPPORT_EMAIL)
        }

        community.setOnClickListener {
            viewModel.onMMCommunityClick()
        }
        binding.btnDeactivateAccount.setOnClickListener {
            currentDialog = createDeactivateDialog()
            currentDialog?.show()
        }
    }

    private fun createDeactivateDialog(): AlertDialog {
        return DialogPattern.create(
            requireContext(),
            getString(R.string.deactivate_title),
            getString(R.string.deactivate_description),
            getString(R.string.deactivate_ok),
            getString(R.string.deactivate_cancel)
        ) {
            currentDialog?.dismiss()
            viewModel.onDeactivateAccount()
        }
    }

    private fun showPhotoDialog() {

        showChangePhotoDialog({
            EzPermission.with(requireContext())
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
                        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, tempPhotoUri)
                        startActivityForResult(
                            cameraIntent,
                            CAMERA_REQUEST_CODE
                        )
                    }
                }
        }, {
            EzPermission.with(requireContext())
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
            viewModel.removeProfileIcon()
        }, viewModel.hasIcon())
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                GALLERY_REQUEST_CODE -> {
                    data?.let {
                        viewModel.onAvatarSelected(data.data!!, cropPhotoUri, this)
                    }
                }
                CAMERA_REQUEST_CODE -> {
                    viewModel.onAvatarSelected(tempPhotoUri, cropPhotoUri, this)
                }
                UCrop.REQUEST_CROP -> {
                    val uri = data?.data ?: return
                    handleImageURI(uri)
                }
            }
        }
    }

    override fun onDetach() {
        listener = null
        super.onDetach()
    }

    private fun handleImageURI(source: Uri) {
        GlobalScope.launch {
            val compressed = activity!!.copyCompressed(source, compressedPhotoPath)
            viewModel.updateProfileIcon(compressed)
        }
    }

    companion object {
        private const val GALLERY_REQUEST_CODE = 42
        private const val CAMERA_REQUEST_CODE = 41
    }
}