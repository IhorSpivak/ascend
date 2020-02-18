package com.doneit.ascend.presentation.video_chat.attachments

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.amazonaws.auth.CognitoCachingCredentialsProvider
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility
import com.amazonaws.regions.Region
import com.amazonaws.regions.Regions
import com.amazonaws.services.s3.AmazonS3Client
import com.androidisland.ezpermission.EzPermission
import com.doneit.ascend.domain.entity.AttachmentType
import com.doneit.ascend.presentation.common.TopListDecorator
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.base.BaseFragment
import com.doneit.ascend.presentation.main.databinding.FragmentAttachmentsBinding
import com.doneit.ascend.presentation.models.PresentationMessage
import com.doneit.ascend.presentation.utils.Constants
import com.doneit.ascend.presentation.utils.Messages
import com.doneit.ascend.presentation.utils.extensions.copyToClipboard
import com.doneit.ascend.presentation.utils.showAddAttachmentDialog
import com.doneit.ascend.presentation.video_chat.attachments.common.AttachmentsAdapter
import com.doneit.ascend.presentation.video_chat.attachments.listeners.PickiTListener
import com.hbisoft.pickit.PickiT
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton


class AttachmentsFragment : BaseFragment<FragmentAttachmentsBinding>() {

    override val viewModelModule = Kodein.Module("AttachmentsFragment") {
        bind<PickiT>() with provider {
            PickiT(
                this@AttachmentsFragment.context,
                object : PickiTListener() {
                    override fun PickiTonCompleteListener(
                        path: String?,
                        wasDriveFile: Boolean,
                        wasUnknownProvider: Boolean,
                        wasSuccessful: Boolean,
                        Reason: String?
                    ) {
                        path?.let {
                            viewModel.uploadFile(path)
                        }
                    }
                }
            )
        }

        bind<AmazonS3Client>() with singleton {
            AmazonS3Client(
                instance<CognitoCachingCredentialsProvider>(), Region.getRegion(
                    Regions.fromName(
                        Constants.AWS_REGION
                    )
                )
            )
        }

        bind<TransferUtility>() with singleton {
            TransferUtility
                .builder()
                .s3Client(instance<AmazonS3Client>())
                .context(this@AttachmentsFragment.context).build()
        }

        bind<ViewModelProvider.Factory>(tag = AttachmentsFragment::class.java) with singleton {
            AttachmentsViewModelFactory(instance(), instance(), instance(), instance())
        }

        bind<AttachmentsContract.ViewModel>() with provider {
            vm<AttachmentsViewModel>(instance(tag = AttachmentsFragment::class.java))
        }
    }

    override val viewModel: AttachmentsContract.ViewModel by instance()
    private val router: AttachmentsContract.Router by instance()
    private val pickit: PickiT by instance()

    private val adapter: AttachmentsAdapter by lazy {
        AttachmentsAdapter({
            requestWritePermissions(getString(R.string.add_attachments_denied)) {
                val directoryType =
                    if (it.attachmentType == AttachmentType.IMAGE) Environment.DIRECTORY_PICTURES else Environment.DIRECTORY_DOWNLOADS
                val basePath = context?.getExternalFilesDir(directoryType)?.path ?: ""

                viewModel.downloadAttachment(it, basePath)
            }
        }, {
            it.link.copyToClipboard(requireContext())
            Toast.makeText(requireContext(), "Copied", Toast.LENGTH_LONG).show()
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

        viewModel.showAddAttachmentDialog.observe(viewLifecycleOwner) {
            showAttachmentDialog()
        }

        viewModel.transferEvents.observe(viewLifecycleOwner, Observer {
            Toast.makeText(requireContext(), getString(it.messageRes), Toast.LENGTH_LONG).show()
        })

        val groupId = arguments!!.getParcelable<AttachmentsArg>(ATTACHMENTS_ARGS)!!.groupId
        viewModel.init(groupId)
    }

    private fun showAttachmentDialog() {
        showAddAttachmentDialog({
            requestWritePermissions(getString(R.string.add_attachments_denied)) {
                val galleryIntent = Intent(Intent.ACTION_PICK)
                galleryIntent.type = "image/*"

                startActivityForResult(
                    galleryIntent,
                    GALLERY_REQUEST_CODE
                )
            }
        }, {
            requestWritePermissions(getString(R.string.gallery_denied)) {
                val fileIntent = Intent(Intent.ACTION_GET_CONTENT)
                //TODO: add filter:
                fileIntent.type = "*/*"

                startActivityForResult(
                    fileIntent,
                    FILE_REQUEST_CODE
                )
            }
        }, {
            val groupId = arguments!!.getParcelable<AttachmentsArg>(ATTACHMENTS_ARGS)!!.groupId
            router.toAddLinkFragment(groupId)
        })
    }

    private fun requestWritePermissions(errorMessage: String, onGranted: () -> Unit) {
        EzPermission.with(context!!)
            .permissions(
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ).request { granted, denied, _ ->

                if (granted.contains(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    onGranted.invoke()
                } else {
                    handleErrorMessage(
                        PresentationMessage(
                            Messages.DEFAULT_ERROR.getId(),
                            content = errorMessage
                        )
                    )
                }
            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                GALLERY_REQUEST_CODE -> {
                    if (data?.data != null) {
                        viewModel.setAttachmentType(AttachmentType.IMAGE)
                        pickit.getPath(data.data, Build.VERSION.SDK_INT)
                    }
                }
                FILE_REQUEST_CODE -> {
                    if (data?.data != null) {
                        viewModel.setAttachmentType(AttachmentType.FILE)
                        pickit.getPath(data.data, Build.VERSION.SDK_INT)
                    }
                }
            }
        }
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