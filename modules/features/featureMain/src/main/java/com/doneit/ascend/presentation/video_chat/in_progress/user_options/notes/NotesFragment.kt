package com.doneit.ascend.presentation.video_chat.in_progress.user_options.notes

import android.os.Bundle
import android.view.KeyEvent
import androidx.lifecycle.Observer
import com.doneit.ascend.presentation.main.base.BaseFragment
import com.doneit.ascend.presentation.main.databinding.FragmentNotesBinding
import com.doneit.ascend.presentation.utils.extensions.showKeyboard
import org.kodein.di.generic.instance

class NotesFragment : BaseFragment<FragmentNotesBinding>() {

    override val viewModelModule = NotesViewModelModule.get(this)
    override val viewModel: NotesContract.ViewModel by instance()

    private val router: NotesContract.Router by instance()

    override fun viewCreated(savedInstanceState: Bundle?) {
        binding.model = viewModel

        val groupId = arguments!!.getLong(GROUP_ID_KEY)
        viewModel.init(groupId)
        binding.text.post {
            showKeyboard(binding.text)
        }
        binding.text.requestFocus()
        viewModel.groupInfo.observe(viewLifecycleOwner, Observer {
            binding.text.setText(it?.note?.content)
            binding.text.text?.length?.let { it -> binding.text.setSelection(it) }
        })

        binding.text.setOnKeyListener { view, i, keyEvent ->
            if (keyEvent.action == KeyEvent.ACTION_DOWN && i == KeyEvent.KEYCODE_ENTER) {
                viewModel.update(binding.text.text.toString())
            }
            return@setOnKeyListener false
        }

        viewModel.navigation.observe(viewLifecycleOwner, Observer {
            handleNavigation(it)
        })
    }

    private fun handleNavigation(action: NotesContract.Navigation) {
        when (action) {
            NotesContract.Navigation.BACK -> {
                viewModel.update(binding.text.text.toString())
                router.onBack()
            }
        }
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