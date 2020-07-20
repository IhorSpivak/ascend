package com.doneit.ascend.presentation.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.common.visible
import com.doneit.ascend.presentation.main.databinding.DialogImageChooserBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ChooseImageBottomDialog(
    private val types: Array<AllowedIntents>,
    private val camera: (() -> Unit) = {},
    private val gallery: (() -> Unit) = {},
    private val video: (() -> Unit) = {}
) : BottomSheetDialogFragment() {
    lateinit var binding: DialogImageChooserBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.SheetDialog)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_image_chooser, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkTypes()
        binding.apply {
            tvCamera.setOnClickListener {
                dismiss()
                camera.invoke()
            }
            tvGallery.setOnClickListener {
                dismiss()
                gallery.invoke()
            }
            tvVideo.setOnClickListener {
                dismiss()
                video.invoke()
            }
            tvCancel.setOnClickListener {
                dismiss()
            }
        }
    }

    private fun checkTypes() {
        binding.apply {
            types.forEach {
                when (it) {
                    AllowedIntents.CAMERA -> tvCamera.visible()
                    AllowedIntents.IMAGE -> tvGallery.visible()
                    AllowedIntents.VIDEO -> tvVideo.visible()
                }
            }
        }
    }

    enum class AllowedIntents {
        CAMERA,
        IMAGE,
        VIDEO
    }

    companion object {
        fun create(
            types: Array<AllowedIntents>,
            camera: (() -> Unit) = {},
            gallery: (() -> Unit) = {},
            video: (() -> Unit) = {}
        ): ChooseImageBottomDialog {
            return ChooseImageBottomDialog(types, camera, gallery, video)
        }
    }
}