package com.doneit.ascend.amazon

import com.amazonaws.auth.CognitoCachingCredentialsProvider
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility
import com.amazonaws.regions.Region
import com.amazonaws.regions.Regions
import com.amazonaws.services.s3.AmazonS3Client
import com.doneit.ascend.presentation.utils.Constants
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton


object AmazonModule {

    fun get() = Kodein.Module("AmazonModule") {

        bind<CognitoCachingCredentialsProvider>() with singleton {
            CognitoCachingCredentialsProvider(
                context,
                Constants.COGNITO_POOL_ID,
                Regions.fromName(Constants.COGNITO_POOL_REGION)
            )
        }

        bind<AmazonS3Client>() with singleton {
            AmazonS3Client(instance<CognitoCachingCredentialsProvider>(), Region.getRegion(Regions.fromName(Constants.AWS_REGION)))
        }

        bind<TransferUtility>() with singleton {
            TransferUtility
                .builder()
                .s3Client(instance<AmazonS3Client>())
                .context(context).build()
        }
    }
}