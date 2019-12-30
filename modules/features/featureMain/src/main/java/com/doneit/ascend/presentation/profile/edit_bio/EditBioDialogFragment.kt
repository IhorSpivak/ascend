package com.doneit.ascend.presentation.profile.edit_bio

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.doneit.ascend.presentation.main.base.BaseFragment
import com.doneit.ascend.presentation.main.databinding.DialogEditBioBinding
import com.doneit.ascend.presentation.profile.common.ProfileViewModel
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider

class EditBioDialogFragment : DialogFragment(), KodeinAware {

    private val _parentKodein: Kodein by closestKodein()

    override val kodein: Kodein = Kodein.lazy {
        extend(_parentKodein, true)
        with(parentFragment) {
            if (this is BaseFragment<*>) {
                extend(kodein, true)
            }
        }
        import(viewModelModule, true)
    }

    private val viewModelModule = Kodein.Module(this::class.java.simpleName) {
        bind<EditBioContract.ViewModel>() with provider { instance<ProfileViewModel>() }
    }
    
    private val viewModel: EditBioContract.ViewModel by instance()

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

        fun newInstance(): EditBioDialogFragment {
            return EditBioDialogFragment()
        }
    }
}