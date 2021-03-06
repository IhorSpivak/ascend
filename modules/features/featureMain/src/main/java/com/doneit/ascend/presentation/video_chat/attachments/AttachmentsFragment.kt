package com.doneit.ascend.presentation.video_chat.attachments

import android.Manifest
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.amazonaws.auth.CognitoCachingCredentialsProvider
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility
import com.amazonaws.regions.Region
import com.amazonaws.regions.Regions
import com.amazonaws.services.s3.AmazonS3Client
import com.androidisland.ezpermission.EzPermission
import com.doneit.ascend.domain.entity.AttachmentType
import com.doneit.ascend.presentation.common.SideListDecorator
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.base.BaseFragment
import com.doneit.ascend.presentation.main.databinding.FragmentAttachmentsBinding
import com.doneit.ascend.presentation.models.PresentationMessage
import com.doneit.ascend.presentation.utils.*
import com.doneit.ascend.presentation.utils.extensions.copyToClipboard
import com.doneit.ascend.presentation.video_chat.attachments.common.AttachmentsAdapter
import com.doneit.ascend.presentation.video_chat.attachments.listeners.PickiTListener
import com.hbisoft.pickit.PickiT
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton
import java.io.File


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

                    override fun PickiTonUriReturned() {
                    }
                }, activity
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
            AttachmentsViewModelFactory(instance(), instance(), instance(), instance(), instance())
        }

        bind<AttachmentsContract.ViewModel>() with provider {
            vm<AttachmentsViewModel>(instance(tag = AttachmentsFragment::class.java))
        }
    }

    override val viewModel: AttachmentsContract.ViewModel by instance()
    private val router: AttachmentsContract.Router by instance()
    private val pickit: PickiT by instance()
    private var lastFileUri = Uri.EMPTY

    private val adapter: AttachmentsAdapter by lazy {
        AttachmentsAdapter({
            requestWritePermissions(getString(R.string.add_attachments_denied)) {
                val directoryType =
                    if (it.attachmentType == AttachmentType.IMAGE) Environment.DIRECTORY_PICTURES else Environment.DIRECTORY_DOWNLOADS
                val basePath = context?.getExternalFilesDir(directoryType)?.path.orEmpty()

                viewModel.downloadAttachment(it, basePath)
            }
        }, {
            it.link.copyToClipboard(requireContext())
            Toast.makeText(requireContext(), "Copied", Toast.LENGTH_LONG).show()
        }, {
            viewModel.onDelete(it)
        }) {
            val dir = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                MediaStore.Downloads.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
            } else Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                .toUri()

            previewFile(File("${dir.path}", it.fileName).toUri())
        }
    }

    override fun viewCreated(savedInstanceState: Bundle?) {
        binding.adapter = this.adapter
        binding.model = viewModel
        binding.isOwner =
            requireArguments().getParcelable<AttachmentsArg>(ATTACHMENTS_ARGS)?.isOwner

        val decorator =
            SideListDecorator(
                paddingTop = resources.getDimension(R.dimen.attachments_list_top_padding).toInt()
            )
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

        viewModel.showPreview.observe(viewLifecycleOwner, Observer { uri ->
            previewFile(uri)
        })

        val groupId = arguments!!.getParcelable<AttachmentsArg>(ATTACHMENTS_ARGS)!!.groupId
        viewModel.init(groupId)
    }

    private fun previewFile(uri: Uri) {
        Intent(Intent.ACTION_VIEW).apply {
            val fileUri = FileProvider.getUriForFile(
                requireContext(),
                requireContext().applicationContext.packageName + ".fileprovider",
                File(uri.path.orEmpty())
            )
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_GRANT_READ_URI_PERMISSION
            data = fileUri
            try {
                startActivity(Intent.createChooser(this, getString(R.string.preview)))
            } catch (e: ActivityNotFoundException) {
                1
            }
        }
    }

    private fun showAttachmentDialog() {
        showAddAttachmentDialog({
            requestWritePermissions(getString(R.string.add_attachments_denied)) {
                val galleryIntent = Intent(Intent.ACTION_PICK).apply {
                    putExtra(Intent.EXTRA_LOCAL_ONLY, true)
                }
                galleryIntent.type = "image/*"

                startActivityForResult(
                    galleryIntent,
                    GALLERY_REQUEST_CODE
                )
            }
        }, {
            requestWritePermissions(getString(R.string.gallery_denied)) {
                val fileIntent = Intent(Intent.ACTION_GET_CONTENT).apply {
                    putExtra(Intent.EXTRA_LOCAL_ONLY, true)
                }
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
            data?.data?.let {
                lastFileUri = it
                when (requestCode) {
                    GALLERY_REQUEST_CODE -> {
                        viewModel.setAttachmentType(AttachmentType.IMAGE)
                    }
                    FILE_REQUEST_CODE -> {
                        viewModel.setAttachmentType(AttachmentType.FILE)
                    }
                }
                validate(it)
            }
        }
    }

    private fun validate(data: Uri) {
        MediaValidator.executeIfUriSupported(requireContext(), lastFileUri,
            onError = {
                when (it) {
                    MediaValidator.ValidationError.SIZE -> showDefaultError(getString(R.string.file_incorrect_size))
                    MediaValidator.ValidationError.FORMAT -> showDefaultError(getString(R.string.file_not_supported))
                }
            },
            action = {
                pickit.getPath(data, Build.VERSION.SDK_INT)
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