package com.doneit.ascend.source.storage.remote.api

import com.doneit.ascend.source.storage.remote.data.response.CardsResponse
import com.doneit.ascend.source.storage.remote.data.response.OKResponse
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Path

interface CardsApi {
    @GET("cards")
    fun getAllCardsAsync(): Deferred<Response<CardsResponse>>

    @DELETE("cards/{id}")
    fun deleteCardAsync(@Path("id") id: Long): Deferred<Response<OKResponse>>
}