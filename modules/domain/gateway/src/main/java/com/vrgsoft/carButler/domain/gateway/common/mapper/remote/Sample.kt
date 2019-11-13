package com.vrgsoft.carButler.domain.gateway.common.mapper.remote

import com.vrgsoft.carButler.domain.entity.Sample
import com.vrgsoft.carButler.domain.gateway.common.orDefault
import com.vrgsoft.carButler.source.storage.remote.data.response.SampleResponse

fun SampleResponse.toEntity(): Sample {
    return Sample(
        id = this@toEntity.id.orDefault()
    )
}