package com.vrgsoft.carButler.domain.use_case.interactor.sample

import com.vrgsoft.carButler.domain.use_case.gateway.ISampleGateway

internal class SampleInteractor(
    private val sampleGateway: ISampleGateway
) : SampleUseCase {

}