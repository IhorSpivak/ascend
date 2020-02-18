package com.doneit.ascend.presentation.video_chat.attachments

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.Observer
import com.amazonaws.mobileconnectors.s3.transferutility.*
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
import com.doneit.ascend.presentation.utils.createTempPhotoUri
import com.doneit.ascend.presentation.utils.extensions.copyToClipboard
import com.doneit.ascend.presentation.utils.showAddAttachmentDialog
import com.doneit.ascend.presentation.video_chat.attachments.common.AttachmentsAdapter
import com.hbisoft.pickit.PickiT
import com.hbisoft.pickit.PickiTCallbacks
import org.kodein.di.generic.instance
import java.io.File


class AttachmentsFragment : BaseFragment<FragmentAttachmentsBinding>(), PickiTCallbacks {

    override val viewModelModule = AttachmentsViewModelModule.get(this)
    override val viewModel: AttachmentsContract.ViewModel by instance()
    private val transferUtility: TransferUtility by instance()
    private val s3Client: AmazonS3Client by instance()

    private val router: AttachmentsContract.Router by instance()

    private val tempPhotoUri by lazy { context!!.createTempPhotoUri() }

    private lateinit var pickit: PickiT

    private val observers: MutableList<TransferObserver> by lazy {
        transferUtility.getTransfersWithType(TransferType.UPLOAD)
    }

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

    override fun onAttach(context: Context) {
        super.onAttach(context)
        pickit = PickiT(context, this)
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
                val fileIntent = Intent(Intent.ACTION_PICK)
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
                        viewModel.setMeta(AttachmentType.IMAGE)
                        pickit.getPath(data.data, Build.VERSION.SDK_INT)
                    }
                }
                FILE_REQUEST_CODE -> {
                    if (data?.data != null) {
                        viewModel.setMeta(AttachmentType.FILE)
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

    override fun PickiTonProgressUpdate(progress: Int) {}

    override fun PickiTonStartListener() {}

    override fun PickiTonCompleteListener(
        path: String?,
        wasDriveFile: Boolean,
        wasUnknownProvider: Boolean,
        wasSuccessful: Boolean,
        Reason: String?
    ) {
        val file = File(path)
        val observer = transferUtility.upload(
            Constants.AWS_BUCKET, file.name, file
        )
        viewModel.setSize(observer.bytesTotal)
        viewModel.setName(file.name)
        observers.add(observer)
        observer.setTransferListener(UploadListener())
    }

    private inner class UploadListener : TransferListener {

        override fun onError(id: Int, e: Exception) {
            Log.e("onError", "Error during upload: $id", e)
        }

        override fun onProgressChanged(id: Int, bytesCurrent: Long, bytesTotal: Long) {
            Log.e("progress", "upload: $id $bytesCurrent $bytesTotal")
        }

        override fun onStateChanged(id: Int, state: TransferState?) {
            when(state) {
                TransferState.COMPLETED -> {
                    viewModel.onFileChosen(s3Client.getUrl(Constants.AWS_BUCKET, viewModel.model.name).toString())
                }
            }
            Log.e("stateChanged", state?.name ?: "")
        }
    }
}