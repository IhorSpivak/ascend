package com.doneit.ascend.presentation.main.bio

import android.os.Bundle
import android.os.Parcelable
import com.doneit.ascend.domain.entity.user.UserEntity
import com.doneit.ascend.presentation.main.base.BaseFragment
import com.doneit.ascend.presentation.main.databinding.FragmentBioBinding
import kotlinx.android.parcel.Parcelize
import org.kodein.di.generic.instance

class BioFragment : BaseFragment<FragmentBioBinding>() {

    private val args by lazy { arguments?.getParcelable<Args>(
        EXTRA_ARGS
    ) }

    override val viewModelModule = BioViewModelModule.get(this)

    override val viewModel: BioContract.ViewModel by instance()

    override fun viewCreated(savedInstanceState: Bundle?) {
        binding.model = viewModel
        args?.user?.let { viewModel.setUser(it) }

        binding.rbRating.setOnRatingBarChangeListener { _, rating, fromUser ->
            if (fromUser) {
                viewModel.setRating(rating.toInt())
            }
        }
    }

    companion object {

        private const val EXTRA_ARGS = "EXTRA_ARGS"

        @Parcelize
        private data class Args(
            val user: UserEntity
        ): Parcelable

        fun getInstance(user: UserEntity) = BioFragment().apply {
            arguments = Bundle().apply {
                putParcelable(EXTRA_ARGS, Args(user))
            }
        }

    }
}