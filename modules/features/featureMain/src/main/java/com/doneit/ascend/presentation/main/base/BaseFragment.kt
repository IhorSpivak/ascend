package com.doneit.ascend.presentation.main.base

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
import com.doneit.ascend.presentation.main.models.PresentationMessage
import com.doneit.ascend.presentation.utils.ConnectionObserver
import com.doneit.ascend.presentation.utils.showNoConnectionDialog
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.KodeinTrigger
import org.kodein.di.android.closestKodein
import org.kodein.di.simpleErasedName
import java.lang.reflect.ParameterizedType

abstract class BaseFragment<B : ViewDataBinding> : Fragment(), KodeinAware {
    //region Kodein

    private val _parentKodein: Kodein by closestKodein()
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

    //endregion

    private val connectionObserver: ConnectionObserver by lazy {
        ConnectionObserver(this@BaseFragment.context!!)
    }

    protected lateinit var binding: B
    private var initialized = false

    //region lifecycle

    override fun onAttach(context: Context) {
        kodeinTrigger.trigger()
        super.onAttach(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)
        if (!initialized) {
            binding = DataBindingUtil.inflate(inflater, getLayoutRes(), container, false)
            initialized = true
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            setLifecycleOwner(this@BaseFragment)
        }

        viewModel.successMessage.observe(this, Observer {
            handleSuccessMessage(it)
        })

        viewModel.errorMessage.observe(this, Observer {
            handleErrorMessage(it)
        })

        viewCreated(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()

        connectionObserver.networkStateChanged.observe(this, Observer {
            if (!it) {
                this.showNoConnectionDialog(getString(R.string.connecting))
            }
        })
    }

    //endregion

    //region abstracts

    abstract fun viewCreated(savedInstanceState: Bundle?)

    //endregion

    //region private methods

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
    open fun handleErrorMessage(message: PresentationMessage) {}

    //endregion

    //region utils

    inline fun <reified VM : BaseViewModelImpl> vm(factory: ViewModelProvider.Factory): VM {
        return ViewModelProviders.of(this, factory)[VM::class.java]
    }

    //endregion
}
