package com.doneit.ascend.presentation.video_chat.in_progress.user_options.notes

import android.os.Bundle
import android.text.InputType
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
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
        binding.text.imeOptions = EditorInfo.IME_ACTION_DONE;
        binding.text.setRawInputType(InputType.TYPE_CLASS_TEXT);
        binding.text.setOnEditorActionListener { textView, actionId, keyEvent ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH
                || actionId == EditorInfo.IME_ACTION_DONE
                || keyEvent.getAction() == KeyEvent.ACTION_DOWN
                && keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                viewModel.update(binding.text.text.toString())
                true
            }
            false
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