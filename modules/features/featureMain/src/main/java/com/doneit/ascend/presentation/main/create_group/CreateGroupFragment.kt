package com.doneit.ascend.presentation.main.create_group

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.androidisland.ezpermission.EzPermission
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.base.CommonViewModelFactory
import com.doneit.ascend.presentation.main.base.argumented.ArgumentedFragment
import com.doneit.ascend.presentation.main.create_group.common.ParticipantAdapter
import com.doneit.ascend.presentation.main.databinding.FragmentCreateGroupBinding
import com.doneit.ascend.presentation.main.extensions.hideKeyboard
import com.doneit.ascend.presentation.main.extensions.vmShared
import com.doneit.ascend.presentation.utils.copyCompressed
import com.doneit.ascend.presentation.utils.getFileExtension
import com.doneit.ascend.presentation.utils.uriToFilePath
import kotlinx.android.synthetic.main.fragment_create_group.*
import kotlinx.android.synthetic.main.view_edit_with_error.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.kodein.di.Kodein
import org.kodein.di.direct
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

class CreateGroupFragment : ArgumentedFragment<FragmentCreateGroupBinding, CreateGroupArgs>() {

    override val viewModelModule = Kodein.Module(this::class.java.simpleName) {
        bind<ViewModelProvider.Factory>() with singleton { CommonViewModelFactory(kodein.direct) }
        bind<ViewModel>(tag = CreateGroupViewModel::class.java.simpleName) with provider {
            CreateGroupViewModel(
                instance(),
                instance(),
                instance(),
                instance()
            )
        }
        bind<CreateGroupContract.ViewModel>() with provider {
            vmShared<CreateGroupViewModel>(
                instance()
            )
        }
    }

    override val viewModel: CreateGroupContract.ViewModel by instance()

    private val adapter: ParticipantAdapter by lazy {
        ParticipantAdapter(mutableListOf(), viewModel)
    }

    override fun viewCreated(savedInstanceState: Bundle?) {
        binding.lifecycleOwner = this
        binding.model = viewModel
        binding.adapter = adapter

        tvTitle.text = getString(R.string.create_group)

        chooseSchedule.editText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                viewModel.chooseScheduleTouch()
                chooseSchedule.clearFocus()
                hideKeyboard()
            }
        }

        startDate.editText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                viewModel.chooseStartDateTouch()
                chooseSchedule.clearFocus()
                hideKeyboard()
            }
        }

        binding.uploadImagePlaceHolder.setOnClickListener {
            pickFromGallery()
        }

        binding.icEdit.setOnClickListener {
            pickFromGallery()
        }
    }

    private fun pickFromGallery() {
        EzPermission.with(context!!)
            .permissions(
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
            .request { granted, _, _ ->
                if (granted.contains(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    val intent = Intent(Intent.ACTION_PICK)
                    intent.type = "image/*"
                    startActivityForResult(intent, GALLERY_REQUEST_CODE)
                }
            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK)
            when (requestCode) {
                GALLERY_REQUEST_CODE -> {
                    val selected = data!!.data

                    selected?.let {
                        val path = activity!!.uriToFilePath(it)
                        handleImageURI(path)
                    }
                }
            }
    }

    private fun handleImageURI(path: String) {
        val fileName = TEMP_IMAGE_NAME + path.getFileExtension()

        GlobalScope.launch {
            val compressed = context!!.copyCompressed(path, fileName)

            launch(Dispatchers.Main) {
                binding.image.setImageURI(Uri.parse(compressed))
                viewModel.createGroupModel.image.observableField.set(compressed)
            }
        }
    }

    companion object {
        private const val GALLERY_REQUEST_CODE = 42
        private const val TEMP_IMAGE_NAME = "group_image_temp"
    }
}