package com.doneit.ascend.presentation.main.create_group.master_mind.webinar

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import androidx.core.widget.addTextChangedListener
import com.androidisland.ezpermission.EzPermission
import com.doneit.ascend.presentation.main.base.argumented.ArgumentedFragment
import com.doneit.ascend.presentation.main.create_group.CreateGroupArgs
import com.doneit.ascend.presentation.main.create_group.CreateGroupHostContract
import com.doneit.ascend.presentation.main.create_group.master_mind.group.CreateGroupFragment
import com.doneit.ascend.presentation.main.databinding.FragmentCreateWebinarBinding
import com.doneit.ascend.presentation.utils.copyCompressed
import com.doneit.ascend.presentation.utils.copyFile
import com.doneit.ascend.presentation.utils.createTempPhotoUri
import com.doneit.ascend.presentation.utils.getCompressedImagePath
import com.redmadrobot.inputmask.MaskedTextChangedListener
import kotlinx.android.synthetic.main.fragment_create_webinar.*
import kotlinx.android.synthetic.main.view_edit_with_error.view.*
import kotlinx.android.synthetic.main.view_multiline_edit_with_error.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider

class CreateWebinarFragment : ArgumentedFragment<FragmentCreateWebinarBinding, CreateGroupArgs>(){
    override val viewModelModule = Kodein.Module(this::class.java.simpleName) {
        bind<CreateWebinarContract.ViewModel>() with provider {
            instance<CreateGroupHostContract.ViewModel>()
        }
    }

    override val viewModel: CreateWebinarContract.ViewModel by instance()
    private val compressedPhotoPath by lazy { context!!.getCompressedImagePath() }
    private val tempPhotoUri by lazy { context!!.createTempPhotoUri() }

    override fun viewCreated(savedInstanceState: Bundle?) {
        binding.apply {
            model = viewModel

            //date & time
            chooseSchedule.multilineEditText.setOnClickListener {
                scrollable_container.requestFocus()
                viewModel.chooseScheduleTouch()
            }
            //end

            dashRectangleBackground.setOnClickListener {
                pickFromGallery()
            }

            //price
            val listener = MaskedTextChangedListener(PRICE_MASK, webinarPrice.editText, object:
                TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                }
            }, object: MaskedTextChangedListener.ValueListener {
                override fun onTextChanged(maskFilled: Boolean, extractedValue: String) {
                }
            })
            webinarPrice.editText.addTextChangedListener(listener)
            webinarPrice.editText.onFocusChangeListener = listener
            //end
        }


        /*icEdit.setOnClickListener {
            pickFromGallery()
        }*/
    }

    private fun pickFromGallery() {

        EzPermission.with(context!!)
            .permissions(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
            )
            .request { granted, _, _ ->
                if (granted.contains(Manifest.permission.READ_EXTERNAL_STORAGE)) {

                    val galleryIntent = Intent(Intent.ACTION_PICK)
                    galleryIntent.type = "image/*"

                    val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, tempPhotoUri)

                    val chooser =
                        Intent.createChooser(galleryIntent, "Select an App to choose an Image")
                    chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf(cameraIntent))

                    startActivityForResult(chooser,
                        GALLERY_REQUEST_CODE
                    )
                }
            }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK)
            when (requestCode) {
                GALLERY_REQUEST_CODE -> {
                    if(data?.data != null) {
                        val galleryPhotoUri = context!!.copyFile(data.data!!, tempPhotoUri)
                        handleImageURI(galleryPhotoUri)
                    } else {
                        handleImageURI(tempPhotoUri)
                    }
                }
            }
    }

    private fun handleImageURI(sourcePath: Uri) {
        GlobalScope.launch {
            val compressed = activity!!.copyCompressed(sourcePath, compressedPhotoPath)

            launch(Dispatchers.Main) {
                viewModel.createGroupModel.image.observableField.set(null)//in order to force observers notification
                viewModel.createGroupModel.image.observableField.set(compressed)
            }
        }
    }

    companion object {
        private const val GALLERY_REQUEST_CODE = 42
        private const val PRICE_MASK = "[0999]{.}[09]"
    }
}