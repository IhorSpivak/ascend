package com.doneit.ascend.presentation.main.base

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.models.PresentationMessage
import com.doneit.ascend.presentation.utils.ConnectionObserver
import com.doneit.ascend.presentation.utils.Messages
import com.doneit.ascend.presentation.utils.extensions.hideKeyboard
import com.doneit.ascend.presentation.utils.showDefaultError
import com.doneit.ascend.presentation.utils.showNoConnectionDialog
import com.doneit.ascend.presentation.views.ConnectionSnackbar
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.KodeinTrigger
import org.kodein.di.android.closestKodein
import org.kodein.di.simpleErasedName
import java.lang.reflect.ParameterizedType

abstract class BaseFragment<B : ViewDataBinding> : Fragment(), KodeinAware {
    //region Kodein

    private val _parentKodein: Kodein by closestKodein()
    var shownDialog: Dialog? = null
    override val kodein: Kodein = Kodein.lazy {
        extend(_parentKodein, true)
        with(parentFragment) {
            if (this is BaseFragment<*>) {
                extend(kodein, true)
            }
        }
        import(viewModelModule, true)
        import(kodeinModule, true)
    }

    open val kodeinModule: Kodein.Module = Kodein.Module("default") {}

    open val viewModelModule: Kodein.Module = Kodein.Module("default2") {}

    override val kodeinTrigger = KodeinTrigger()

    abstract val viewModel: BaseViewModel

    private var noConnectionDialog: ConnectionSnackbar? = null
    private val connectionObserver: ConnectionObserver by lazy {
        ConnectionObserver(context!!)
    }

    private val progressDialog: Dialog by lazy {
        LayoutInflater.from(context).inflate(R.layout.dialog_progress, null, false).let {
            AlertDialog.Builder(context, R.style.ProgressDialogStyle)
                .setCancelable(false)
                .setView(it)
                .create()
        }
    }

    //endregion

    protected var binding: B by AutoClearedValue()
    private var initialized = false

    //region lifecycle

    override fun onResume() {
        super.onResume()
        connectionObserver.networkStateChanged.observe(this, Observer {
            onNetworkStateChanged(it)
        })
    }

    override fun onPause() {
        noConnectionDialog?.dismiss()
        connectionObserver.networkStateChanged.removeObservers(this)
        super.onPause()
    }

    override fun onAttach(context: Context) {
        kodeinTrigger.trigger()
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        binding = DataBindingUtil.inflate(inflater, getLayoutRes(), container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            lifecycleOwner = this@BaseFragment
        }

        viewModel.successMessage.observe(this) {
            it?.let {
                handleSuccessMessage(it)
            }
        }

        viewModel.errorMessage.observe(this) {
            it?.let {
                handleErrorMessage(it)
            }
        }

        viewModel.progressDialog.observe(this) {
            it?.let {
                showProgress(it)
            }
        }

        //initDialog()

        if (savedInstanceState?.getBoolean(IS_PROGRESS_SHOWN_KEY) == true) {
            progressDialog.show()
        }

        viewCreated(savedInstanceState)
    }

    override fun onDestroyView() {
        hideKeyboard()
        super.onDestroyView()
    }

    //endregion

    //region abstracts

    abstract fun viewCreated(savedInstanceState: Bundle?)

    //endregion

    protected fun showProgress(isDialogShown: Boolean) {
        if (isDialogShown) {
            progressDialog.show()
        } else {
            progressDialog.dismiss()
        }
    }
    protected open fun onNetworkStateChanged(hasConnection: Boolean){
        if (hasConnection) {
            noConnectionDialog?.dismiss()
        } else {
            noConnectionDialog =
                view?.showNoConnectionDialog(getString(R.string.connecting))
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        if (progressDialog != null) {
            outState.putBoolean(IS_PROGRESS_SHOWN_KEY, progressDialog.isShowing)
        }
        progressDialog?.dismiss()
        noConnectionDialog?.dismiss()
    }

    //region private methods

    /*private fun initDialog() {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_progress, null, false)
        progressDialog = AlertDialog.Builder(context, R.style.ProgressDialogStyle)
            .setCancelable(false)
            .setView(dialogView)
            .create()
    }*/

    open fun getLayoutRes(): Int {
        var superClassGeneric = this.javaClass.genericSuperclass
        var superClass = this.javaClass.superclass

        while (superClassGeneric !is ParameterizedType) {
            if (superClass != null) {
                superClassGeneric = superClass.genericSuperclass
                superClass = superClass.superclass
            } else {
                throw Exception("maybe something with BaseFragment?")
            }
        }

        val fragmentLayoutName = superClassGeneric.actualTypeArguments[0]
            .simpleErasedName()
            .replace("Binding", "")
            .split("(?<=[a-z])(?=[A-Z])|(?<=[A-Z])(?=[A-Z][a-z])".toRegex())
            .joinToString(separator = "_")
            .toLowerCase()

        val resourceName = "${context?.applicationContext?.packageName}:layout/$fragmentLayoutName"
        return resources.getIdentifier(resourceName, null, null)
    }

    open fun handleSuccessMessage(message: PresentationMessage) {}
    open fun handleErrorMessage(message: PresentationMessage) {
        when (message.id) {
            Messages.DEFAULT_ERROR.getId() -> {
                showDefaultError(message.content!!)
            }
        }
    }

    //endregion

    //region utils

    inline fun <reified VM : BaseViewModelImpl> vm(factory: ViewModelProvider.Factory): VM {
        return ViewModelProviders.of(this, factory)[VM::class.java]
    }

    //endregion

    companion object {
        private const val IS_PROGRESS_SHOWN_KEY = "IS_PROGRESS_SHOWN_KEY"
    }
}
