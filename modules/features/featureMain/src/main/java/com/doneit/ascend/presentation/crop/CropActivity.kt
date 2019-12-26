package com.doneit.ascend.presentation.crop

import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.base.BaseActivity
import com.doneit.ascend.presentation.main.base.CommonViewModelFactory
import com.yalantis.ucrop.UCrop
import com.yalantis.ucrop.UCrop.EXTRA_ASPECT_RATIO_X
import com.yalantis.ucrop.UCrop.EXTRA_ASPECT_RATIO_Y
import com.yalantis.ucrop.UCropFragment
import com.yalantis.ucrop.UCropFragmentCallback
import org.kodein.di.Kodein
import org.kodein.di.direct
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

class CropActivity : BaseActivity(), UCropFragmentCallback {

    override fun diModule() = Kodein.Module("CropActivity") {
        bind<ViewModelProvider.Factory>() with singleton { CommonViewModelFactory(kodein.direct) }
        bind<CropRouter>() with provider {
            CropRouter(
                this@CropActivity
            )
        }
    }

    fun getContainerId() = R.id.container

    private val router: CropRouter by instance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crop)

        val source: Uri = intent.getParcelableExtra(ARG_SOURCE) as Uri
        val destination: Uri = intent.getParcelableExtra(ARG_DESTINATION) as Uri

        val options = UCrop.Options()

//
//        options.setActiveWidgetColor(ContextCompat.getColor(activity, R.color.background_dimmed))
        options.setCropFrameColor(ContextCompat.getColor(this, R.color.background_dimmed))
//        options.setRootViewBackgroundColor(ContextCompat.getColor(activity,R.color.background_dimmed))
        options.setDimmedLayerColor(ContextCompat.getColor(this, R.color.dimmed_black))
//        options.setToolbarColor(ContextCompat.getColor(activity, R.color.dimmed_black))
//        options.setStatusBarColor(ContextCompat.getColor(activity, R.color.background_dimmed))

        options.setToolbarCancelDrawable(R.drawable.ic_btn_cancel_circle)
        options.setToolbarCropDrawable(R.drawable.ic_btn_ok_circle)

        options.setShowCropGrid(false)
        options.setHideBottomControls(true)
        options.setShowCropFrame(false)
        options.setToolbarTitle(getString(R.string.move_and_scale))
        options.withAspectRatio(1f, 1f)
        options.withMaxResultSize(325, 325)
        options.setCompressionFormat(Bitmap.CompressFormat.PNG)
        options.setCompressionQuality(80)
        options.setCircleDimmedLayer(true)

        val optionBundle = options.optionBundle
        optionBundle.putFloat(EXTRA_ASPECT_RATIO_X, 1f)
        optionBundle.putFloat(EXTRA_ASPECT_RATIO_Y, 1f)

        optionBundle.putParcelable(UCrop.EXTRA_INPUT_URI, source)
        optionBundle.putParcelable(UCrop.EXTRA_OUTPUT_URI, destination)

        router.navigateToCrop(UCropFragment.newInstance(optionBundle))
    }

    companion object {
        const val ARG_SOURCE = "arg_source"
        const val ARG_DESTINATION = "arg_destination"
    }

    override fun onCropFinish(result: UCropFragment.UCropResult?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun loadingProgress(showLoader: Boolean) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}