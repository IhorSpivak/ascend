package com.doneit.ascend.presentation.video_chat.attachments

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import com.androidisland.ezpermission.EzPermission
import com.doneit.ascend.presentation.common.TopListDecorator
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.base.BaseFragment
import com.doneit.ascend.presentation.main.databinding.FragmentAttachmentsBinding
import com.doneit.ascend.presentation.models.PresentationMessage
import com.doneit.ascend.presentation.utils.Messages
import com.doneit.ascend.presentation.utils.copyFile
import com.doneit.ascend.presentation.utils.createTempPhotoUri
import com.doneit.ascend.presentation.utils.extensions.copyToClipboard
import com.doneit.ascend.presentation.utils.showAddAttachmentDialog
import com.doneit.ascend.presentation.video_chat.attachments.common.AttachmentsAdapter
import org.kodein.di.generic.instance

class AttachmentsFragment : BaseFragment<FragmentAttachmentsBinding>() {


    override val viewModelModule = AttachmentsViewModelModule.get(this)
    override val viewModel: AttachmentsContract.ViewModel by instance()

    private val router: AttachmentsContract.Router by instance()

    private val tempPhotoUri by lazy { context!!.createTempPhotoUri() }

    private val adapter: AttachmentsAdapter by lazy {
        AttachmentsAdapter({
            //TODO: implement
            Toast.makeText(requireContext(), "download", Toast.LENGTH_LONG).show()
        }, {
            //TODO: refactor
            it.link.copyToClipboard(requireContext())
            Toast.makeText(requireContext(), "copy", Toast.LENGTH_LONG).show()
        }, {
            viewModel.onDelete(it)
        })
    }

    override fun viewCreated(savedInstanceState: Bundle?) {
        binding.adapter = this.adapter
        binding.model = viewModel
        val decorator =
            TopListDecorator(resources.getDimension(R.dimen.attachments_list_top_padding).toInt())
        binding.rvAttachments.addItemDecoration(decorator)
        binding.addAttachments.setOnClickListener {
            viewModel.onAddAttachmentClick()
        }

        viewModel.attachments.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })

        viewModel.navigation.observe(viewLifecycleOwner, Observer {
            handleNavigation(it)
        })

        viewModel.showAddAttachmentDialog.observe(this) {
            showAttachmentDialog()
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                GALLERY_REQUEST_CODE -> {
                    if (data?.data != null) {
                        val galleryPhotoUri = context!!.copyFile(data.data!!, tempPhotoUri)
                        viewModel.onPhotoChosen(galleryPhotoUri)
                    }
                }
                FILE_REQUEST_CODE -> {
                    if (data?.data != null) {
                        viewModel.onFileChosen()
                    }
                }
            }
        }
    }

    private fun showAttachmentDialog() {
        showAddAttachmentDialog({
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
            EzPermission.with(context!!)
                .permissions(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ).request { granted, denied, _ ->

                    if (denied.isEmpty().not()) {
                        viewModel.errorMessage.call(
                            PresentationMessage(
                                Messages.DEFAULT_ERROR.getId(),
                                content = getString(R.string.add_attachments_denied)
                            )
                        )
                    }

                    if (granted.contains(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                        val fileIntent = Intent(Intent.ACTION_PICK)
                        //TODO: add filter:
                        fileIntent.type = "*/*"

                        startActivityForResult(
                            fileIntent,
                            FILE_REQUEST_CODE
                        )
                    }
                }
        }, {
            // TODO: add url
            router.toAddLinkFragment()
        })
    }

    private fun handleNavigation(action: AttachmentsContract.Navigation) {
        when (action) {
            AttachmentsContract.Navigation.BACK -> router.onBack()
        }
    }

    companion object {
        private const val GALLERY_REQUEST_CODE = 42
        private const val FILE_REQUEST_CODE = 43

        private const val ATTACHMENTS_ARGS = "ATTACHMENTS_ARGS"

        fun newInstance(args: AttachmentsArg): AttachmentsFragment {
            val fragment = AttachmentsFragment()
            fragment.arguments = Bundle().apply {
                putParcelable(ATTACHMENTS_ARGS, args)
            }
            return fragment
        }
    }
}