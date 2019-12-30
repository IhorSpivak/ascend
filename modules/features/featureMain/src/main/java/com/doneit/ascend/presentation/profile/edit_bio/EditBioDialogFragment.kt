package com.doneit.ascend.presentation.profile.edit_bio

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.doneit.ascend.presentation.main.databinding.DialogEditBioBinding

class EditBioDialogFragment : DialogFragment() {

    private lateinit var initValue: String
    private lateinit var viewModel: EditBioContract.ViewModel

    override fun onStart() {
        super.onStart()

        if (dialog != null) {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.MATCH_PARENT
            this.dialog?.window?.setLayout(width, height)
            this.dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.WHITE))
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = DialogEditBioBinding.inflate(inflater)
        binding.model = viewModel
        binding.lifecycleOwner = this

        with(binding) {

            btnBack.setOnClickListener {
                dismiss()
            }

            btnSave.setOnClickListener {
                val newValue = etBio.text
                viewModel.updateBio(newValue)
            }
        }

        return binding.root
    }

    companion object {

        const val TAG = "BioDialogFragment"

        fun newInstance(
            initValue: String,
            viewModel: EditBioContract.ViewModel
        ): EditBioDialogFragment {
            return EditBioDialogFragment().apply {
                this.viewModel = viewModel
                this.initValue = initValue
            }
        }
    }
}