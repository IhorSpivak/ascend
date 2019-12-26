package com.doneit.ascend.presentation.crop

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.base.BaseActivity
import com.doneit.ascend.presentation.main.base.CommonViewModelFactory
import com.yalantis.ucrop.UCrop
import com.yalantis.ucrop.UCropFragment
import com.yalantis.ucrop.UCropFragmentCallback
import kotlinx.android.synthetic.main.activity_crop.*
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


        options.setCropFrameColor(ContextCompat.getColor(this, R.color.background_dimmed))
        options.setDimmedLayerColor(ContextCompat.getColor(this, R.color.dimmed_black))

        options.setShowCropGrid(false)
        options.setHideBottomControls(true)
        options.setShowCropFrame(false)
        options.setToolbarTitle(getString(R.string.move_and_scale))
        options.withAspectRatio(1f, 1f)
        options.withMaxResultSize(325, 325)
        //options.setCompressionFormat(Bitmap.CompressFormat.PNG)
        //options.setCompressionQuality(80)
        options.setCircleDimmedLayer(true)

        val uCrop = UCrop.of(source, destination)
        uCrop.withOptions(options)

        val fragment = UCropFragment.newInstance(uCrop.getIntent(this).extras)
        router.navigateToCrop(fragment)

        fragment.setCallback(this)

        cancel.setOnClickListener {
            router.goBack()
        }

        ok.setOnClickListener {
            fragment.cropAndSaveImage()
        }
    }

    override fun onCropFinish(result: UCropFragment.UCropResult?) {
        when (result?.mResultCode) {
            RESULT_OK -> {
                handleCropResult(result.mResultData)
            }
            UCrop.RESULT_ERROR -> {
                var message = UCrop.getError(result.mResultData)?.message
                if(message == null) {
                    message = resources.getString(R.string.toast_unexpected_error)
                }
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun loadingProgress(showLoader: Boolean) {
    }

    private fun handleCropResult(result: Intent) {
        val resultUri = UCrop.getOutput(result)
        if (resultUri != null) {
            val intent = Intent()
            intent.data = resultUri
            setResult(RESULT_OK, intent)
            router.goBack()
        } else {
            Toast.makeText(
                this,
                R.string.toast_cannot_retrieve_cropped_image,
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    companion object {
        const val ARG_SOURCE = "arg_source"
        const val ARG_DESTINATION = "arg_destination"
    }
}