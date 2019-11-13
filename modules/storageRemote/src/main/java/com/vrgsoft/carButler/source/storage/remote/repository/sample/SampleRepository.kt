package com.vrgsoft.carButler.source.storage.remote.repository.sample

import com.vrgsoft.core.remote.BaseRepository
import com.vrgsoft.carButler.source.storage.remote.api.SampleApi

internal class SampleRepository(
    private val api: SampleApi
) : BaseRepository(), ISampleRepository {
}