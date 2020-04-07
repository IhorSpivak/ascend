package com.doneit.ascend.presentation.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.databinding.DialogImageChooserBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ChooseImageBottomDialog(
    private val camera: (() -> Unit),
    private val gallery: (() -> Unit)
): BottomSheetDialogFragment() {
    lateinit var binding: DialogImageChooserBinding
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
        binding.apply {
            tvCamera.setOnClickListener {
                dismiss()
                camera.invoke()
            }
            tvGallery.setOnClickListener {
                dismiss()
                gallery.invoke()
            }
            tvCancel.setOnClickListener {
                dismiss()
            }
        }
    }

    companion object{
        fun create(
            camera: (() -> Unit),
            gallery: (() -> Unit)
        ): ChooseImageBottomDialog{
            return ChooseImageBottomDialog(camera, gallery)
        }
    }
}