package com.doneit.ascend.source.storage.remote.api

import com.doneit.ascend.source.storage.remote.data.response.CardsResponse
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.GET

interface CardsApi {
    @GET("cards")
    fun getAllCards(): Deferred<Response<CardsResponse>>
}