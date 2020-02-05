package com.doneit.ascend.presentation.video_chat.in_progress.user_options.notes

import android.os.Bundle
import com.doneit.ascend.presentation.main.base.BaseFragment
import com.doneit.ascend.presentation.main.databinding.FragmentNotesBinding
import org.kodein.di.generic.instance

class NotesFragment : BaseFragment<FragmentNotesBinding>() {

    override val viewModelModule = NotesViewModelModule.get(this)
    override val viewModel: NotesContract.ViewModel by instance()

    override fun viewCreated(savedInstanceState: Bundle?) {
        binding.model = viewModel

        val groupId = arguments!!.getLong(GROUP_ID_KEY)
        viewModel.init(groupId)
    }

    companion object {
        private const val GROUP_ID_KEY = "GROUP_ID_KEY"

        fun newInstance(groupId: Long): NotesFragment {
            val fragment = NotesFragment()
            fragment.arguments = Bundle().apply {
                putLong(GROUP_ID_KEY, groupId)
            }

            return fragment
        }
    }
}