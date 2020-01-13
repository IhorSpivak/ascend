package com.doneit.ascend.source.storage.remote.api

import com.doneit.ascend.source.storage.remote.data.request.CreateCardRequest
import com.doneit.ascend.source.storage.remote.data.response.CardResponse
import com.doneit.ascend.source.storage.remote.data.response.CardsResponse
import com.doneit.ascend.source.storage.remote.data.response.OKResponse
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.*

interface CardsApi {

    @POST("cards")
    fun createCard(@Body request: CreateCardRequest): Deferred<Response<CardResponse>>

    @GET("cards")
    fun getAllCardsAsync(): Deferred<Response<CardsResponse>>

    @DELETE("cards/{id}")
    fun deleteCardAsync(@Path("id") id: Long): Deferred<Response<OKResponse>>
}